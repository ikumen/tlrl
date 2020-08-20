import { User } from '../../user/types';

export interface WebUrl {
  id?: number;
  url: string;  
}

export enum ReadStatus {
  NA = 'na', 
  READ = 'read', 
  UNREAD = 'unread',
}

export enum SharedStatus {
  PRIVATE = 'private',
  PUBLIC = 'public'
}

export type Tag = {
  id: string,
  count?: number
}

export interface Bookmark {
  id: number;
  webUrl: WebUrl;
  owner: User;
  title: string;
  tags?: Tag[];
  description: string;
  readStatus: ReadStatus;
  sharedStatus: SharedStatus;
  createdDateTime: Date;
  updatedDateTime: Date;
  archivedDateTime: Date
}
