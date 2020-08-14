import React, { useContext } from 'react';
import { ReadStatus, Bookmark } from './types';
import BookmarkContext from './Context';

const nextStatus = {
  [ReadStatus.UNREAD]: ReadStatus.READ,
  [ReadStatus.NA]: ReadStatus.UNREAD,
  [ReadStatus.READ]: ReadStatus.NA,
}

const statusStyles = {
  [ReadStatus.NA]: 'gray',
  [ReadStatus.UNREAD]: 'orange',
  [ReadStatus.READ]: 'green'
}

function ReadStatusComponent({bookmark}: {bookmark: Bookmark}) {
  const bookmarkContext = useContext(BookmarkContext);

  return <span tabIndex={0} className="pr2 tab underline-hover pointer" 
    onClick={() => bookmarkContext.update!({id: bookmark.id, readStatus: nextStatus[bookmark.readStatus]})}>
    <i className={`material-icons v-btm md-med mr1 ${statusStyles[bookmark.readStatus]}`}>menu_book</i>
    {('' + bookmark.readStatus).toLocaleLowerCase()}
  </span>
};

export default ReadStatusComponent;