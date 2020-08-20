import React from 'react';
import SearchOrAddForm from './SearchOrAddForm';
import { Link  } from 'react-router-dom';

type Props = {
  searchEnabled?: boolean,
  addEnabled?: boolean
}

function Header(props: Props) {
  return (
    <header className="fl cf w-100 pv2 ph1 ph3-m ph6-l bg-near-white bb b--black-10" id="header">
      <div className="fl dib w-10 nowrap pointer">
        <Link to={'/'} tabIndex={1} className="tab">
        <i className="material-icons md-lg orange v-mid">bookmark_border</i><span className="dib-m dib-l fw6 f6 orange">TLRL</span>
        </Link>
      </div>
      <SearchOrAddForm />
    </header> 
  );
}

export default Header;