import { User } from "../user/types";

export enum AuthStatus {
  NA = 'NA',
  READY = 'READY',
  AUTHENTICATED = 'AUTHENTICATED',
  UNCONFIRMED = 'UNCONFIRMED',
  UNAUTHENTICATED = 'UNAUTHENTICATED'
}

export type AuthState = {
  status: AuthStatus,
  user?: User,
  csrf?: string
}

export const INIT_AUTH_STATE: AuthState = {
  status: AuthStatus.UNAUTHENTICATED,
  user: undefined,
  csrf: undefined
}

