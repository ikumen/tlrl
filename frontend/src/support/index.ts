import { useState, useCallback, useRef, useEffect } from "react";

/** Parse given querystring (e.g, some/url?param=1) to dictionary of key/values */
export function parseQueryString(qs: string): Map<string, string> {
  return (qs || '')
    .substr(1)    // skip the ?
    .split('&')   // tokenize into name=value pairs
    .map(s => decodeURI(s))   // decode any values that have been encoded
    .filter(s => s.trim())    // skip empty values   
    .filter(s => s.length)    // skip empty values
    .reduce((map: Map<string, string>, s) => {
      const [ key, value ] = s.split('=');
      map.set(key, value);
      return map;
    }, new Map<string, string>());
}

export const getCookies = () => {
  const cookies = document.cookie.split('; ')
    .reduce((map: Map<string, string>, cookie) => {
      const [name, value] = cookie.split('=');
      map.set(name, value);
      return map;
    }, new Map<string, string>());

  return {
    get: (name: string, defaultValue?: string) => {
      return cookies.has(name) ? cookies.get(name) : defaultValue;
    }
  }
}

export function useAsyncError() {
  const [ , setError ] = useState();
  return useCallback(
    (err) => { setError(() => { throw err; });
    }, [setError])
}

type FetchOptions = {
  url?: string,
  method?: 'DELETE'|'GET'|'POST'|'PATCH',
  headers?: {[key:string]: string},
  data?: any
}

const APPLICATION_JSON = 'application/json';

export const defaultHeaders = {
  'Content-Type': APPLICATION_JSON,
  'Accept': APPLICATION_JSON
}

export function usePrevious<T>(value: T) {
  const ref = useRef<T>();
  useEffect(() => {
    ref.current = value;
  });
  return ref.current;
}

/** A throw away function */
export const noOp = (x?:any) => {}


