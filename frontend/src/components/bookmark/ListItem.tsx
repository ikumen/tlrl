import React, { useState, useContext } from 'react';
import { Bookmark } from './types';
import { Editable, DateTime } from '..';
import ReadStatus from './ReadStatus';
import SharedStatus from './SharedStatus';
import Tags from './Tags';
import BookmarkContext from './Context';

const btnStyle = "f7 fw3 bw0 br1 ph3 pv1 ml2 dib pointer";

type ListItemProps = {
  bookmark: Bookmark;
  onSelect: (id: number) => void;
  onDeselect: (id: number) => void;
  selected: boolean;
}

function BookmarkListItem(props: ListItemProps) {
  const { bookmark } = props;
  const bookmarkContext = useContext(BookmarkContext);
  const [ editable, setEditable ] = useState(false);
  const [ partial, setPartial ] = useState<Partial<Bookmark>>({
    id: bookmark.id, title: bookmark.title, description: bookmark.description});

  const onChange = (attrName: string, value: any) => {
    setPartial({...partial, [attrName]: value})
  };

  const onUpdate = () => bookmarkContext.update!(partial).then(() => setEditable(false));
  const enableEditable = () => setEditable(true);
  const disableEditable = () => setEditable(false);

  const selectItem = (evt: React.SyntheticEvent<HTMLInputElement, Event>) => {
    if (evt.currentTarget.checked) {
      props.onSelect(bookmark.id);
    } else {
      props.onDeselect(bookmark.id);
    }
  }

  return <li key={bookmark.id} className="flex flex-column pa1 bg-near-white mb3">
    {/* ---- url ---- */}
    <div className="w-100 nowrap mb1 pa0">
      <span className="fl w-95">
        <a href={bookmark.webUrl.url} className="fl f6 w-100 link purple truncate">{bookmark.webUrl.url}</a>
      </span>
      <span className="fr w-05 tr">
        <input type="checkbox" className="v-top" onChange={selectItem} checked={props.selected}></input>
      </span>
    </div>

    {/* ---- title ---- */}
    <h2 className="fl w-100 f4 fw6 pv0 mt0 mb2">
      <Editable id="title" 
        shouldTruncate={true}
        editable={editable}
        value={bookmark.title || bookmark.webUrl.url} 
        onChange={onChange}
      />
    </h2>

    {/* ---- description ---- */}
    <div className="fl w-100 pv0 mt0 mb2">
      <Editable id="description" 
        editable={editable} 
        value={bookmark.description || ''}
        onChange={onChange}
      />
    </div>

    {/* ---- tags ---- */}
    <div className="fl w-100 pv0 mt0 mb3">
      <Tags id="tags" editable={editable} tags={bookmark.tags} onChange={onChange} /> 
    </div>

    {/* ---- statuses and meta ---- */}
    <div className="fl w-100 f6">
      <div className="fl w-50 black-70">
        <ReadStatus bookmark={bookmark} /> 
        <SharedStatus bookmark={bookmark} />
        <div className="fl w-100 mt2 f6">
          {bookmark.archivedDateTime && <a className="link" href={`archive/${bookmark.owner.id}/${bookmark.id}`} target="_new"><i className="material-icons v-mid mr1 md-small">launch</i> archive</a>}
        </div>
      </div>
      <div className="fr w-50 tr">
        <div className="fr w-100 black-70">
          <DateTime date={bookmark.createdDateTime} /> by @{bookmark.owner.name}
        </div>
      {editable ?
        <div className="fr w-100 mt2">
          <button className={`${btnStyle} white bg-gray`} onClick={disableEditable}>Cancel</button>
          <button className={`${btnStyle} white bg-green`} onClick={onUpdate}>Done</button>
        </div> :
        <div className="fr w-100 mt2">
          <button className={`${btnStyle} always bg-near-white underline-hover`} onClick={enableEditable}>Edit</button>
        </div>
        }
      </div>
    </div>
  </li>  
}

export default BookmarkListItem;

