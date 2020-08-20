import React, { useEffect, useContext } from 'react';
import * as Bookmark from '../components/bookmark';
import { Pagination } from '../components';
import { parseQueryString } from '../support';
import { useLocation } from 'react-router-dom';

function Search() {
  const location = useLocation();
  const params = parseQueryString(location.search);
  const ctx = useContext(Bookmark.BookmarkContext);
  useEffect(() => {
    ctx.get!(`/search${location.search}`);
  }, [location]);

  const terms = params.get('terms');
  return <>
    <div className="f4 fw5 mb2">Search Results {terms ? `"${terms}"`: ``}</div>
    {ctx.bookmarks && ctx.bookmarks.length 
      ? <>
          <Bookmark.BookmarkList bookmarks={ctx.bookmarks} total={ctx.pagingDetails?.total || 0} />
          <Pagination {...ctx.pagingDetails!} />
        </>
      : <div className="fl w-100 pv3 ph1 tc">No results found for "{parseQueryString(location.search).get('terms')}"</div>
    }
  </>;
}


export default Search;  