import React from 'react';
import { BookmarkletButton } from '../components';

function Help() {
  return (
    <main className="fl cf w-100 ph1 ph3-m ph6-l bg-white">     
      <div className="fl w-100 ph0 pv2">
        <h2 className="black-80">Help</h2>
        <h3 className="fw5">Adding bookmarks</h3>
        <p>
          TLRL provides a <a href="//en.wikipedia.org/wiki/Bookmarklet">bookmarklet</a> to save pages from your browser to our application. Simply drag this bookmarklet ( <BookmarkletButton /> ) up to your browsers bookmark toolbar.
        </p>      
      </div>
    </main>
  );
}

export default Help;