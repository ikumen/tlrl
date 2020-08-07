import React from 'react';
import { noOp } from '../support';
import { AuthState, INIT_AUTH_STATE } from './types';

export type AuthContextType = {
  authState: AuthState,
  handleAuthError: (resp: Response) => void,
  signInChoices: {[key:string]: string},
  signIn: (provider: string) => void,
  signOut: () => void,
  signUp: (name: string) => Promise<any>
}

const AuthContext = React.createContext<AuthContextType>({
  authState: INIT_AUTH_STATE,
  handleAuthError: noOp,
  signInChoices: {},
  signIn: noOp,
  signOut: noOp,
  signUp: () => Promise.resolve()
});

export default AuthContext;