import React from 'react';
import { useLocation, Link } from 'react-router-dom';
import { AuthStatus, AuthState } from '../auth';

type ErrorProp = {
  message: string,
  status?: number,
  authState?: AuthState
}

function Doh({ error, clearError }: {error: ErrorProp, clearError: Function}) {
  const location = useLocation<{status: number, url: string, message: string}>();
  const authenticated = error.authState!.status === AuthStatus.AUTHENTICATED;
  const clear = () => clearError();

  return <>
    <header className={`fl cf w-100 pv2 ph1 ph3-m ph6-l bg-washed-yellow`} id="header">
      <div className="fl dib w-10 nowrap pointer">
        <Link to={location} onClick={clear} tabIndex={1}>
          <i className="material-icons md-large orange v-mid">bookmark_border</i><span className="dib-m dib-l fw6 f6 orange">TLRL</span>
        </Link>
      </div>
    </header> 

    <main className={`fl cf w-100 ph1 ph3-m ph6-l bg-washed-yellow`}>     
      <div className="fl w-100 ph0 pv2">
        <h2 className="black-80">Doh!!</h2>
        How embarrassing, looks like there's an error with our server or a possible network issue, <Link className="link" to={location} onClick={clear}>please try again later</Link>.
        <ul className="white list ph1 pv2 ma0 mt3 hover-bg-washed-red">
          <li>url: {location.pathname}{location.search}</li>
          <li>status: {error.status}</li>
          <li>message: {error.message}</li>
        </ul>
      </div>
    </main>
    <footer className="footer">
    <div className="fl w-100 pt3 pb3 ph1 ph3-m ph6-l flex items-center gray bt b--light-gray">
      <div className="w-20 fl f7 nowrap">&copy; <a href="//github.com/ikumen" className="link">Thong Nguyen</a></div>
      <div className="w-80 tr f6">
        <a className="ml3 link" href="//github.com/tlrl/tlrl-app">about</a>        
        { !authenticated && <Link className="ml3 link" onClick={clear} to="/signin">signin</Link> }
      </div>
    </div>
  </footer>
  </>;
}

export default Doh;