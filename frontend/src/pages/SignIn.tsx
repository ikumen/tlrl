import React, { useContext } from 'react';
import { AuthContext } from '../auth';
import './SignIn.css'

function SignIn() {
  const { signIn, signInChoices } = useContext(AuthContext);

  return (<main className="fl cf w-100 ph1 ph3-m ph6-l bg-washed-yellow">
    <div className="fl w-100 ph5-l pv2 tc">
      <h1 className="mv1">Welcome to TLRL</h1>
      <h3 className="mt2 mb4 fw4">Searchable, archivable, no-nonsense bookmarking</h3>
      <p className="cf ph3-l tc">
        Learn <a href="//github.com/ikumen">more about TLRL</a> or sign in with an authentication provider below to get started.
      </p>
      <ul className="cf w-100 list f6 tc">
        {Object.entries(signInChoices!).map(([id, name]) =>
        <li key={id} className="pv2 mv2 nowrap">
          <button 
              className={`link br2 pv1 ph3 btn ${id}`} 
              onClick={() => signIn!(id)}>
            Sign in with {name}
          </button>
        </li>)}
      </ul>      
    </div>
  </main>);
}

export default SignIn;