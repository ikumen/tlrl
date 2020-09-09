import React, { PropsWithChildren, useState, useEffect } from 'react';
import { Role } from '../user/types';
import { AuthState, AuthStatus, INIT_AUTH_STATE } from './types';
import AuthContext, { AuthContextType } from './context';
import useFetch from '../support/useFetch';

// TODO: not sure where to put this
const signInChoices = {
  github: 'GitHub', 
  google: 'Google'
}

function AuthProvider({children}: PropsWithChildren<any>) {
  const [ authState, setAuthState ] = useState<AuthState>(INIT_AUTH_STATE);
  const api = useFetch();
  
  /* Helper for parsing out a User from a fetch response */
  const parseUserResponse = (resp: Response): any => {
    if (resp.ok) return resp.json();
    return {
      name: resp.headers.get('user-name')!,
      email: resp.headers.get('user-email')!,
      roles: [Role.UNCONFIRMED]          
    }
  }

  /* Helper for handling a fetch response, setting appropriate states on error */
  const handleResponse = (resp: any) => {
    if (resp.status === 401) {
      setAuthState(INIT_AUTH_STATE);
    } else if (resp.ok || resp.status === 403) {
      const user = parseUserResponse(resp);
      setAuthState({user, 
        status: resp.ok ? AuthStatus.AUTHENTICATED : AuthStatus.UNCONFIRMED,
        csrf: resp.headers.get('x-csrf-token')
      });
    }
    return resp;
  }

  let defaultContext: AuthContextType;

  /** The context, consisting of it's state and the available operations on it's state. */
  defaultContext = {
    /** State being wrapped by the AuthContext */
    authState, 
    /** The available "sign in" providers (e.g, Google, GitHub...) */
    signInChoices,
    /**
     * Sign in the current user to the given provider.
     */
    signIn: (provider: string) => {
      window.location.href = `/oauth2/authorization/${provider}`;
    },
    handleAuthError: (resp: Response) => {
      if (resp.status === 401 || resp.status === 403) {
        setAuthState(INIT_AUTH_STATE);
      }
    },
    /**
     * Sign out the currently authenticated user.
     */
    signOut: () => api.post('/api/user/signout', {authContext: defaultContext})
      .then(() => setAuthState(INIT_AUTH_STATE)),
    /**
     * Sign up (create) the currently authenticated user. The user is authenticated 
     * by the provider, but not in our system until they sign up.
     */
    signUp: (name: string) => api.post('/api/user/signup', 
        { authContext: defaultContext, data: {name}, willHandleBadRequest: true})
      .then(handleResponse),
  }

  /* 
   * Every time we load up the "auth" context, try to fetch the current user from our backend.
   */
  useEffect(() => {
    api.get('/api/user', {authContext: defaultContext}).then(handleResponse);
  }, [])
  
  return (
    <AuthContext.Provider value={defaultContext}>
      {children}
    </AuthContext.Provider>
  );  
}

export default AuthProvider;
