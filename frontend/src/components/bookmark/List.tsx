import React, { useState, useEffect } from 'react';
import { Bookmark } from './types';
import BookmarkListItem from './ListItem';
import EditBookmarksToolBar from './ToolBar';
import { usePrevious, parseQueryString } from '../../support';
import { useLocation, Link } from 'react-router-dom';
 
/**
 * Component responsible for display a list {@link Bookmark}s per specified page.
 * @param {number} props.page the page to load and display
 */
function BookmarkList({bookmarks = [], total = 0}: {bookmarks: Bookmark[], total: number}) {
  const [ selectedItems, setSelectedItems ] = useState<number[]>([]);
  const prevBookmarks = usePrevious<Bookmark[]>(bookmarks);
  const location = useLocation();
  const params = parseQueryString(location.search);

  // When ever the pagingation changes, we load that specific page 
  // and capture the paging details for Pagination component.
  useEffect(() => {
    if ((!prevBookmarks && bookmarks) || (prevBookmarks?.length !== bookmarks.length)) {
      deselectAllItems();
    }
  }, [bookmarks, prevBookmarks]);

  /** Helper for adding item to selectedItems list */
  const selectItem = (id: number) => setSelectedItems(itemIds => [id, ...itemIds]);
  /** Helper for adding all items to selectedItems list */
  const selectAllItems = () => setSelectedItems(bookmarks.map(b => b.id));
  /** Helper for removing an item from selectedItems list */
  const deselectItem = (id: number) => setSelectedItems(ids => (ids.filter(_id => _id !== id)));
  /** Helper for removing all items from selectedItems list */
  const deselectAllItems = () => setSelectedItems([]);

  /** Helper for selecting all or none of the bookmark items */
  const toggleAllSelected = () => {
    if (selectedItems.length === 0) {
      selectAllItems();
    } else {
      deselectAllItems();
    }
  }

  const filters = [params.get('readStatus'), params.get('sharedStatus')].filter(s => (s !== undefined));
  const tags = params.get('tags') ? params.get('tags')!.split(',') : [];

  return (
    <div className="fl w-100">
      <div className="f6">
        <span className="f5">{total} </span>
        {filters.map((s, i) => <>
          <Link to={"#"}>{s}</Link>{i + 1 < filters.length ? `, ` : ` `}
        </>)}
        bookmark{total !== 1 ? `s` : ``}
        {tags.length ? ` tagged` : ``}
        {tags.map(t => <span className="ml1 pv0 ph2 bg-light-yellow br1">{t}</span> )}
        {(tags.length || filters.length) ? <> (<Link to="/">clear all</Link>)</> : ``}

      </div>
      <EditBookmarksToolBar 
        selectedItems={selectedItems}
        toggleAllSelected={toggleAllSelected}
        enabled={selectedItems.length > 0}
      /> 
      <ul className="fl w-100 list pa0 ma0">
        { bookmarks.map(bookmark => 
          <BookmarkListItem key={bookmark.id} 
            bookmark={bookmark}
            selected={selectedItems.includes(bookmark.id)}
            onDeselect={deselectItem}
            onSelect={selectItem}
          />) 
        }      
      </ul>
    </div>
  );
}

export default BookmarkList;
