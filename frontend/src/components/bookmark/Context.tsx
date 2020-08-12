import React, { useState, useContext } from 'react';
import { PropsWithChildren } from "react";
import { Bookmark, SharedStatus, ReadStatus } from './types';
import useFetch from '../../support/useFetch';
import { AuthContext } from '../../auth';
import { PagingDetails } from '../Pagination';

type BookmarkContextType = {
  pagingDetails: PagingDetails,
  bookmarks: Bookmark[],
  all: (opts?: {page: number}) => Promise<any>,
  search: (terms: string, opts: {page: number}) => Promise<any>,
  create: (partial: Partial<Bookmark>) => Promise<Bookmark>,
  update: (partial: Partial<Bookmark>) => Promise<Partial<Bookmark>|undefined>,
  deleteAll: (ids: number[]) => Promise<number[]>,
  sharedStatus: (ids: number[], status: SharedStatus) => Promise<number[]>,
  readStatus: (ids: number[], status: ReadStatus) => Promise<number[]>
}

const BookmarkContext = React.createContext<Partial<BookmarkContextType>>({bookmarks: []})

export function Provider({children}: PropsWithChildren<any>) {
  const [ bookmarks, setBookmarks ] = useState<Bookmark[]>([]);
  const [ pagingDetails, setPagingDetails ] = useState<PagingDetails>({first: false, last: false, page: 0, size: 10, total: 0});
  const authContext = useContext(AuthContext);
  const api = useFetch({authContext, baseUrl: '/api/bookmarks'});

  const handleResults = async (resp: Response) => {
    if (!resp.ok) return;
    const results = await resp.json();
    setBookmarks(results.content);
    setPagingDetails({
      first: results.first, 
      last: results.last,
      page: results.number,
      size: results.size,
      total: results.totalElements
    });
    return results;
  }

  const defaultContext: BookmarkContextType = {
    pagingDetails,
    bookmarks,
    /**
     * Return all Bookmarks for the currently authenticated user.
     */
    all: (opts: {page: number} = {page: 0}) => 
      api.get(`?page=${opts.page}`).then(handleResults),
    /**
     * Search Bookmarks for content that matches given terms. 
     */
    search: (terms: string, opts: {page: number} = {page: 0}) => 
      api.get(`/search?q=${terms ? terms.trim() : ''}&page=${opts.page}`).then(handleResults)
    ,
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