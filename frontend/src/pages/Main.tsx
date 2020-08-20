import React from 'react';
import * as Bookmark from '../components/bookmark';
import { Switch, Route} from 'react-router-dom';
import Bookmarks from './Bookmarks';
import Search from './Search';
import { Facets } from '../components';

function Main() {
  return (
    <main className="fl cf w-100 ph1 ph3-m ph6-l bg-white">     
      <Bookmark.Provider>
      <div className="fl w-80-l w-100 ph0">
        <Switch>
          <Route exact={true} path="/search" component={Search} />
          <Route component={Bookmarks} />
        </Switch>
      </div>
      <div className="fl w-20 pl3">
        <Facets />
      </div>
      </Bookmark.Provider>  
    </main>
  );
}

export default Main;  