import { User } from '../../user/types';

export interface WebUrl {
  id?: number;
  url: string;  
}

export enum ReadStatus {
  NA = 'NA', 
  READ = 'READ', 
  UNREAD = 'UNREAD',
}

export enum SharedStatus {
  PRIVATE = 'PRIVATE',
  PUBLIC = 'PUBLIC'
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
