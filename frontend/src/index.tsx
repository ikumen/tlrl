import React from 'react';
import ReactDOM from 'react-dom';
import App from './App';
import * as serviceWorker from './serviceWorker';
import { BrowserRouter as Router } from 'react-router-dom';
import ErrorBoundry from './ErrorBoundry';
import { AuthProvider } from './auth';

ReactDOM.render(
  <React.StrictMode>
    <Router>
      <ErrorBoundry>
        <AuthProvider>
          <App />
        </AuthProvider>
      </ErrorBoundry>
    </Router>
  </React.StrictMode>,
  document.getElementById('root')
);

// If you want your app to work offline and load faster, you can change
// unregister() to register() below. Note this comes with some pitfalls.
// Learn more about service workers: https://bit.ly/CRA-PWA
serviceWorker.unregister();
