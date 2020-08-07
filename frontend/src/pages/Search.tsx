import React, { useEffect, useContext } from 'react';
import * as Bookmark from '../components/bookmark';
import { Pagination } from '../components';
import { parseQueryString } from '../support';
import { useLocation } from 'react-router-dom';

function Search() {
  const location = useLocation();
  const ctx = useContext(Bookmark.BookmarkContext);
  
  useEffect(() => {
    const params = parseQueryString(location.search);
    const page = params.has('page') ? Number(params.get('page')) : 0;
    ctx.search!((params.get('terms') || ''), {page});
  }, [location]);

  return <>
    {ctx.bookmarks && ctx.bookmarks.length 
      ? <><Bookmark.BookmarkList bookmarks={ctx.bookmarks} />
          <Pagination {...ctx.pagingDetails!} /></>
      : <div className="fl w-100 pv4 ph1 tc">No results found for "{parseQueryString(location.search).get('terms')}"</div>
    }
  </>;
}


export default Search;  