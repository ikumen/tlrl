import React, { useContext } from 'react';
import { Link } from 'react-router-dom';
import { AuthContext, AuthStatus } from '../auth';

function Footer() {
  const { authState, signOut } = useContext(AuthContext);

  return (
    <footer className="footer">
    <div className="fl w-100 pt3 pb3 ph1 ph3-m ph6-l flex items-center gray bt b--light-gray">
      <div className="w-20 fl f7 nowrap">&copy; <a href="//github.com/ikumen" className="link">Thong Nguyen</a></div>
      <div className="w-80 tr f6">
        <a className="ml3 link" href="//github.com/ikumen/tlrl">about</a>        
        { authState.status === AuthStatus.AUTHENTICATED
          ? <>
            <Link className="ml3 link" to={'/help'}>help</Link>
            <Link to="#" className="ml3 link bg-white ba bw0" onClick={signOut}>signout</Link>
            </>
          : <Link className="ml3 link" to="/signin">signin</Link> 
        }
      </div>
    </div>
  </footer>
  );
}

export default Footer;