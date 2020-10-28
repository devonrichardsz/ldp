export interface IComment {
  author: string;
  text: string;
  children: string[];
  likes: string[];
  createdAt: Date;
  modifiedAt?: Date;
  id: string;
}

export interface IPost {
  title: string;
  thumbnail?: string;
  body: string;
  summary: string;
  likes: string[];
  comments: string[];
  author: string;
  createdAt: Date;
  modifiedAt?: Date;
  id: string;
}
