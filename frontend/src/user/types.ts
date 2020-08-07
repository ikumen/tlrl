export interface User {
  id?: number,
  name: string,
  roles: Role[],
  email: string,
}

export enum Role {
  USER = 'ROLE_USER',
  UNCONFIRMED = 'ROLE_UNCONFIRMED'
}
