import { IUser } from 'types/User';

export interface IComment {
  user: IUser;
  text: string;
  children: [IComment];
  likes: [string];
  createdAt: Date;
  modifiedAt?: Date;
}

export interface IPost {
  title: string;
  body: string;
  likes: [string];
  comments: [IComment];
  author: string;
  createdAt: Date;
  modifiedAt?: Date;
  id: string;
}
