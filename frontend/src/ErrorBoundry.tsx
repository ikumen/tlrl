import React, { PropsWithChildren, ReactNode } from 'react';
import { AuthState } from './auth';
import { Doh } from './pages';

type Props = PropsWithChildren<ReactNode> & {
  errorPage?: string
}

type State = {
  error?: {
    message: string,
    status?: number,
    authState?: AuthState
  },
  info?: any
}

/**
 * Error boundry (https://reactjs.org/docs/error-boundaries.html) that simplies 
 * forwards (via history.push) to an error page, with the caught Error embedded
 * in the history state.
 */
class ErrorBoundry extends React.Component<Props, State> {
  constructor(props: Props) {
    super(props);
    this.state = {};
    this.reset = this.reset.bind(this);
  }
 
  componentDidCatch(error: any, info: any) {
    //console.log('did catch error:', error, info);
    this.setState({ info, error });
  }

  reset() {
    this.setState({error: undefined, info: undefined});
  }

  render() {
    const { error } = this.state;
    return ( error 
      ? <Doh error={error} clearError={this.reset} />
      : this.props.children
    );
  }
}

export default ErrorBoundry;