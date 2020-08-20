import { defaultHeaders, useAsyncError } from './index';
import { AuthContextType } from '../auth';

type Opts = {
  method?: 'DELETE'|'GET'|'PATCH'|'POST',
  headers?: {[key: string]: string},
  authContext?: AuthContextType,
  data?: any,
  willHandleBadRequest?: boolean,
}

function useFetch(cfg: {
    baseUrl?: string,
    defaultHeaders?: {[key:string]: string},
    authContext?: AuthContextType,
    willHandleBadRequest?: boolean,
  } = {
    baseUrl: '', 
    defaultHeaders,
    willHandleBadRequest: false
  }) 
{
  const asyncError = useAsyncError();

  async function _fetch(url: string, {method, headers, data, authContext, willHandleBadRequest}: Opts = {willHandleBadRequest: false}) {
    const { handleAuthError, authState } = authContext || cfg.authContext || {};
    headers = {...defaultHeaders, ...headers, 'x-csrf-token': (authState ? authState.csrf! : '')};
    const opts = (method === 'GET'
      ? { method, headers }
      : { method, headers, body: data ? JSON.stringify(data) : null});
    
    return await window.fetch(`${cfg.baseUrl}${url}`, opts)
      .then(resp => {
        if (!resp.ok) throw resp;
        return resp;
      }).catch(resp => {
        if (resp.status === 401 || resp.status === 403) 
          handleAuthError!(resp);
        else if (!willHandleBadRequest) {
          asyncError({
            status: resp.status,
            message: resp.statusText,
            authState
          }); 
        }
        return resp;
      });
  }

  return {
    delete: (url: string, opts?: Opts) => _fetch(url, {...opts, method: 'DELETE'}),
    get: (url: string, opts?: Opts) => _fetch(url, {...opts, method: 'GET'}),
    patch: (url: string, opts?: Opts) => _fetch(url, {...opts, method: 'PATCH'}),
    post: (url: string, opts?: Opts) => _fetch(url, {...opts, method: 'POST'}),
  }

}

export default useFetch;

