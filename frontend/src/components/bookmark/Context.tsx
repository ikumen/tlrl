import React, { useState, useContext } from 'react';
import { PropsWithChildren } from "react";
import { Bookmark, SharedStatus, ReadStatus } from './types';
import useFetch from '../../support/useFetch';
import { AuthContext } from '../../auth';
import { PagingDetails } from '../Pagination';

type BookmarkContextType = {
  pagingDetails: PagingDetails,
  bookmarks: Bookmark[],
  facets: Facets,
  get: (url: string) => Promise<any>,
  create: (partial: Partial<Bookmark>) => Promise<Bookmark>,
  update: (partial: Partial<Bookmark>) => Promise<Partial<Bookmark>|undefined>,
  deleteAll: (ids: number[]) => Promise<number[]>,
  sharedStatus: (ids: number[], status: SharedStatus) => Promise<number[]>,
  readStatus: (ids: number[], status: ReadStatus) => Promise<number[]>
}

type Facets = {
  tags: {id: string, count: number}[],
  sharedStatuses: {[key: string]: number},
  readStatuses: {[key: string]: number}
}
const BookmarkContext = React.createContext<Partial<BookmarkContextType>>({bookmarks: []})

export function Provider({children}: PropsWithChildren<any>) {
  const [ bookmarks, setBookmarks ] = useState<Bookmark[]>([]);
  const [ facets, setFacets ] = useState<Facets>({tags: [], sharedStatuses: {}, readStatuses: {}});
  const [ pagingDetails, setPagingDetails ] = useState<PagingDetails>({first: false, last: false, page: 0, size: 10, total: 0});
  const authContext = useContext(AuthContext);
  const api = useFetch({authContext, baseUrl: '/api/bookmarks'});

  const handleResults = async (resp: Response) => {
    if (!resp.ok) return;
    const results = await resp.json();
    setBookmarks(results.bookmarks);
    setFacets({
      tags: results.facets.tags,
      sharedStatuses: {'private': 0, 'public': 0, ...results.facets.sharedStatuses},
      readStatuses: {'na': 0, 'unread': 0, 'read': 0, ...results.facets.readStatuses}
    });

    setPagingDetails({
      first: results.first, 
      last: results.last,
      page: results.page,
      size: results.size,
      total: results.total
    });
    return results;
  }

  const defaultContext: BookmarkContextType = {
    pagingDetails,
    bookmarks,
    facets,
    get: (url) => api.get(`${url}`).then(handleResults),
    /**
     * Create the given Bookmark.
     * @param partial {@link Partial<Bookmark>} with attributes to save.
     */
    create: (partial: Partial<Bookmark>) => (
      api.post(``, {data: partial}).then(resp => {
        if (!resp.ok) return;
        const bookmark = resp.json();
        setBookmarks(bookmarks => [bookmark, ...bookmarks]);
        return bookmark;
      })
    ),
    update: (partial: Partial<Bookmark>) => (
      api.patch(`/${partial.id}`, {data: partial}).then(resp => {
        if (!resp.ok) return;
        setBookmarks((bookmarks: Bookmark[]) => bookmarks.map(bookmark => (
          bookmark.id === partial.id ? {...bookmark, ...partial} : bookmark
        )));
        return partial;
      })
    ),
    /**
     * Delete Bookmark with a matching id.
     */
    deleteAll: (ids: number[]) => (
      api.delete(``, {data: ids}).then(resp => {
        if (!resp.ok) return [];
        setBookmarks((bookmarks: Bookmark[]) => bookmarks.filter(b => !ids.includes(b.id)));
        return ids;
      })
    ),
    /**
     * Update the SharedStatus for Bookmarks in the given id list.
     */
    sharedStatus: (ids: number[], sharedStatus: SharedStatus) => (
      api.patch(`/shared/${sharedStatus}`, {data: ids}).then(resp => {
        if (!resp.ok) return [];
        setBookmarks((bookmarks: Bookmark[]) => bookmarks.map(bookmark => (
            ids.includes(bookmark.id) ? {...bookmark, sharedStatus} : bookmark)));
        return ids;
      })
    ),
    /**
     * Update the ReadStatus for Bookmarks in the given id list.
     */
    readStatus: (ids: number[], readStatus: ReadStatus) => (
      api.patch(`/read/${readStatus}`, {data: ids}).then(resp => {
        if (!resp.ok) return [];
        setBookmarks((bookmarks: Bookmark[]) => bookmarks.map(bookmark => (
          ids.includes(bookmark.id) ? {...bookmark, readStatus} : bookmark)));
        return ids;
      })
    ),
  }

  return <BookmarkContext.Provider value={defaultContext}>
    {children}
  </BookmarkContext.Provider>
}

export default BookmarkContext;