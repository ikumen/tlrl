import React from 'react';
import * as Bookmark from '../components/bookmark';
import { Switch, Route } from 'react-router-dom';
import Bookmarks from './Bookmarks';
import Search from './Search';
import RelatedTags from '../components/RelatedTags';

function Main() {
  return (
    <main className="fl cf w-100 ph1 ph3-m ph6-l bg-white">     
      <div className="fl w-80-l w-100 ph0">
      <Bookmark.Provider>
        <Switch>
          <Route exact={true} path="/search" component={Search} />
          <Route component={Bookmarks} />
        </Switch>
      </Bookmark.Provider>  
      </div>
      <div className="fl w-20 pl3">
        <RelatedTags />
      </div>
    </main>
  );
}

export default Main;  