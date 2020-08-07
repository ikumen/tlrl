import React, { useContext } from 'react';
import { Bookmark, SharedStatus } from './types';
import BookmarkContext from './Context';

const defaultIconClass = "material-icons v-btm md-small mr1";

function togglePrivacy(bookmark: Bookmark) {
  return bookmark.sharedStatus === SharedStatus.PRIVATE 
    ? SharedStatus.PUBLIC : SharedStatus.PRIVATE;
}

function SharedStatusComponent({bookmark}: {bookmark: Bookmark}) {
  const bookmarkContext = useContext(BookmarkContext);

  return <span tabIndex={0} className="ph2 underline-hover pointer" 
    onClick={() => bookmarkContext.update!({id: bookmark.id, sharedStatus: togglePrivacy(bookmark)})}>
    {bookmark.sharedStatus === SharedStatus.PRIVATE 
      ? <i className={`${defaultIconClass} green`}>lock</i>
      : <i className={`${defaultIconClass} red`}>lock_open</i>
    }{('' + bookmark.sharedStatus).toLocaleLowerCase()}
  </span>
};

export default SharedStatusComponent;