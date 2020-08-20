import React, { useState, useContext } from 'react';
import { useHistory } from 'react-router-dom';
import * as Bookmark from '../components/bookmark';
import { AuthContext, AuthStatus } from '../auth';

const buttonStyle = "pv1 ph2 ph3-l f6 ml1 tab bw0 br1 pointer fw3 white";

function SearchOrAddForm() {
  const history = useHistory();
  const authContext = useContext(AuthContext);
  const ctx = useContext(Bookmark.BookmarkContext);
  const [ urlOrTerm, setUrlOrTerm ] = useState<string>();
  const inputRef = React.createRef<HTMLInputElement>();
  
  const clearInput = () => {
    if (inputRef && inputRef.current)
      inputRef.current.value = '';
  };

  const handleAdd = (evt: React.MouseEvent<HTMLButtonElement, MouseEvent>) => {
    evt.preventDefault();
    if (!urlOrTerm) {
      alert('Please enter a url to bookmark.')
    } else {
      ctx.create!({webUrl: {url: urlOrTerm}}).then(clearInput);
    }
  }

  const handleSearch = (evt: React.FormEvent<HTMLFormElement>) => {
    evt.preventDefault();
    // very simple impl, just takes list of terms to use against an OR search
    // TODO: validate/clean up
    // TODO: add ability to specify AND and grouping
    const queryTerms = urlOrTerm ? `?terms=${urlOrTerm}` : '';
    history.push(`/search${queryTerms}`);
  }

  const onUrlOrTermChange = (evt: React.ChangeEvent<HTMLInputElement>) => {
    setUrlOrTerm(evt.currentTarget.value);
  }

  return (
    <form className="fl w-90 nowrap tr" id="search-n-add-form" onSubmit={handleSearch}>
      <input type="text" className="w-60-l w-50-m w-40 ph2 pv1 f6 bw0 br1" 
        placeholder="search or url to add" 
        tabIndex={2} 
        ref={inputRef}
        onChange={onUrlOrTermChange}
      />

      <div className="dib nowrap w-20-l tl">
        <button id="search-btn" type="submit" 
          className={`b--blue bg-blue ${buttonStyle}`} 
          tabIndex={3}
          >Search</button>

        {/* {authContext.authState.status === AuthStatus.AUTHENTICATED &&
        <button id="add-page-btn" type="button" 
          className={`b--green bg-green ${buttonStyle}`} 
          onClick={handleAdd}
          tabIndex={4}>
            Add
        </button>} */}
      </div>
    </form>
  );
}

export default SearchOrAddForm;