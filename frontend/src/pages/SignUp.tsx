import React, { useContext, useState } from 'react';
import { AuthContext } from '../auth';
import { useHistory, Link } from 'react-router-dom';


function SignUp() {
  const [ errors, setErrors ] = useState<string[]>([]);
  const { authState, signUp, signOut } = useContext(AuthContext);
  const history = useHistory();
  const nameFieldRef = React.createRef<HTMLInputElement>();

  const handleSignUp = () => {
    if (nameFieldRef.current?.checkValidity()) {
      setErrors([]);
      const name = nameFieldRef.current.value
      signUp!(name)
        .then(async (resp: Response) => {
          if (resp.ok)
            history.replace({pathname: '/'});
          else {
            const { errors, message } = await resp.json();
            setErrors((resp.status === 409 ? [message] 
              : errors ? errors : [`We are unable to process the request, please try again later.`]));
          }
        });
    }
  }

  return (<main className="fl cf w-100 ph1 ph3-m ph6-l bg-washed-yellow">
    <div className="fl w-100 ph3-l pv2">
      <h1 className="mv1 tc">TLRL Sign Up</h1>
      <div className="mt3 mb4 tc">
        <span className="fw4 pa2 bg-light-green">{authState.user?.email}</span>
      </div>
      <div className="cf tl">
        It looks like this is your first time signing into TLRL. Let's create
        an account so we can remember the awesome web pages you'll be bookmarking. 
        We just need a user name, and you're set.         
      </div>
      <div className="fl w-100 mt3 mb4">
        <label className="fl w-100" htmlFor="name">
        <ul className={`${errors.length ? '' : 'dn '}list red ma0 pa0 f6`}>{errors.map((e,i) => <li key={i}>
          {e}
        </li>)}
        </ul>
        </label>
        <div>
          <input type="text" id="name" className="mv2 mr2" 
            ref={nameFieldRef} 
            required={true} 
            pattern={"^[a-zA-Z0-9_]*$"} 
            minLength={3} 
          /> 
          <button className="pointer" onClick={handleSignUp}>let's go</button>
        </div>
        <label className="fl w-100 i f6">(Note: must be 3+ chars, can contain alphanumeric and dashes)</label>
        <div className="red"></div>        
      </div>
      <div className="cf mt4 tl">
        If you've signed in by mistake, no worries&mdash;we didn't save anything yet&mdash;
        just <Link to="#" onClick={signOut}>click here to remove any sessions</Link> you have with us.
      </div>
    </div>
  </main>
  );
}

export default SignUp;