import React, { useContext } from 'react';
import { Switch, Route, Redirect } from 'react-router-dom';
import { Footer, Header } from './components';
import { AuthContext, AuthStatus } from './auth';
import { Help, SignIn, SignUp } from './pages';
import './App.css';
import Main from './pages/Main';

function UnauthenticatedUserApp() {
  return <>
    <Route component={SignIn} />
    <Footer />
  </>;
}

function UnconfirmedUserApp() {
  return <>
    <Route component={SignUp} />
    <Footer />
  </>;
}

function AuthenticatedUserApp() {
  return <>
    <Header />
    <Switch>
      <Route exact={true} path="/help" component={Help} />
      <Route exact={true} path="/recent" component={Main} />
      <Route exact={true} path="/signin" render={() => <Redirect to="/" />} />
      <Route component={Main} />    
    </Switch>
    <Footer />
  </>;
}

function App() {
  const { authState } = useContext(AuthContext);

  switch (authState.status) {
    case AuthStatus.AUTHENTICATED:
      return AuthenticatedUserApp();
    case AuthStatus.UNCONFIRMED:
      return UnconfirmedUserApp();
    default:
      return UnauthenticatedUserApp();
  }
}

export default App;
