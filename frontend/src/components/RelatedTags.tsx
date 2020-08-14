import React, { useEffect, useState } from 'react';
import { useLocation } from 'react-router-dom';
import useFetch from '../support/useFetch';
import { Tag } from './bookmark/types';

function RelatedTags() {
  const location = useLocation();
  const api = useFetch({baseUrl: "/api/bookmarks"});
  const [ tags, setTags ] = useState<Tag[]>([])

  useEffect(() => {
    api.get("/tags").then(resp => resp.json())
      .then(setTags);
  }, [location])

  return <div>
    <h4 className="mt2 black-80">Related Tags</h4>
    <ul className="list f6 ma0 pa0">
    {tags && tags.map(tag => <li key={tag.id} className="mv2">
      {tag.id} <span className="f7 gray">x {tag.count}</span>
    </li>)}

    </ul>
  </div>
}

export default RelatedTags;