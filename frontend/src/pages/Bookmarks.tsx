import React, { useEffect, useContext } from 'react';
import * as Bookmark from '../components/bookmark';
import { Pagination, BookmarkletButton } from '../components';
import { useLocation } from 'react-router-dom';

function Bookmarks() {
  const location = useLocation();
  const ctx = useContext(Bookmark.BookmarkContext);
    
  useEffect(() => {
    let unmounted = false;
    if (!unmounted) {
      ctx.get!(`${location.search}`);
    }
    return () => { unmounted = true };
  }, [location.pathname, location.search]);

  return <>
    {ctx.bookmarks && ctx.bookmarks.length 
      ? <>
          <div className="f4 fw5 mb2">All Bookmarks</div>
          <Bookmark.BookmarkList bookmarks={ctx.bookmarks!} total={ctx.pagingDetails?.total || 0}/>
          <Pagination {...ctx.pagingDetails!} /></>
      : <div className="fl w-100 pv4 ph1 tc">
        You don't have any bookmarks saved. To get start, drag this bookmarklet <BookmarkletButton /> to your browser's bookmark toolbar and click it for pages you want to save.
      </div>
    }
  </>;
}

export default Bookmarks;  