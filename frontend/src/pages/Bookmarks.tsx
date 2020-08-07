import React, { useEffect, useContext } from 'react';
import * as Bookmark from '../components/bookmark';
import { Pagination, BookmarkletButton } from '../components';
import { parseQueryString } from '../support';
import { useLocation } from 'react-router-dom';

function Bookmarks() {
  const location = useLocation();
  const ctx = useContext(Bookmark.BookmarkContext);
    
  useEffect(() => {
    let unmounted = false;
    if (!unmounted) {
      const params = parseQueryString(location.search);
      const page = params.has('page') ? Number(params.get('page')) : 0;
      ctx.all!({page});
    }
    return () => { unmounted = true };
  }, [location.pathname, location.search]);

  return <>
    {ctx.bookmarks && ctx.bookmarks.length 
      ? <><Bookmark.BookmarkList bookmarks={ctx.bookmarks!} />
          <Pagination {...ctx.pagingDetails!} /></>
      : <div className="fl w-100 pv4 ph1 tc">
        You don't have any bookmarks saved. To get start, drag this bookmarklet <BookmarkletButton /> to your browser's bookmark toolbar and click it for pages you want to save.
      </div>
    }
  </>;
}

export default Bookmarks;  