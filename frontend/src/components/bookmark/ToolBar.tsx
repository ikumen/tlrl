import React, { useContext } from 'react';
import { ReadStatus, SharedStatus } from './types';
import BookmarkContext from './Context';

const defaultStyles = 'f7 fw5 bw0 br1 ph3 pv1 ml2 dib';

type Props = {
  enabled: boolean;
  selectedItems: number[];
  toggleAllSelected: () => void; 
}

function EditBookmarksToolBar({toggleAllSelected, selectedItems, enabled}: Props) {
  const styles = `${defaultStyles} ${enabled ? 'clickable bg-black-10 tab' : 'bg-near-white tab'}`;
  const goBtnStyles = `${styles} ${enabled ? 'black underline-hover' : 'black-20'}`;
  const deleteBtnStyles = `${styles} white ${enabled ? 'bg-red underline-hover' : 'bg-washed-red'}`;

  const bookmarkContext = useContext(BookmarkContext);
  const selectRef = React.createRef<HTMLSelectElement>();

  const clearSelectedAction = () => {
    if (selectRef.current) {
      selectRef.current.selectedIndex = 0;
    }
  }

  const onSetAction = (evt: React.FormEvent<HTMLFormElement> | React.MouseEvent<HTMLButtonElement, MouseEvent>) => {
    evt.preventDefault();
    const value = selectRef.current!.value || '';
    if (/^(NA|UNREAD|READ)$/.test(value)) {
      bookmarkContext.readStatus!(selectedItems, ReadStatus[value as keyof typeof ReadStatus])
        .then(clearSelectedAction);
    } else if (/^(PUBLIC|PRIVATE)$/.test(value)) {
      bookmarkContext.sharedStatus!(selectedItems, SharedStatus[value as keyof typeof SharedStatus])
        .then(clearSelectedAction);
    } else {
      alert('Please pick a valid action!');
    }
  }

  return <form className={`fl w-100 pv2 ph1 tr`} onSubmit={onSetAction}>
    {/* <span className="f6 fl">Filter</span><i className="material-icons v-mid md-small mr1">filter_list</i>
    <span className="mh2 bar"></span> */}

    <button type="button" disabled={!enabled} 
      onClick={() => bookmarkContext.deleteAll!(selectedItems)} 
      className={`${deleteBtnStyles}`}>Delete
    </button>
    <span className="ml2 bar"></span>

    <select className={`${styles}`} ref={selectRef} disabled={!enabled}>
      <option>Select action</option>
      <option>------</option>
      <option value="NA">Clear Read/Unread</option>
      <option value="UNREAD">Mark as Unread</option>
      <option value="READ">Mark as Read</option>
      <option>------</option>
      <option value="PRIVATE">Make private</option>
      <option value="PUBLIC">Make public</option>
    </select>
    <button type="submit" disabled={!enabled} onClick={onSetAction} className={`${goBtnStyles} black-80`}>Go</button>
    <input type="checkbox" className="ml2" checked={selectedItems.length > 0} onChange={toggleAllSelected}></input>
  </form>
}

export default EditBookmarksToolBar;