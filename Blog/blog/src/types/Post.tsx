export interface IPost {
  title: string;
  body: string;
  likes: [string];
  comments: [string];
  author: string;
  createdAt: Date;
  modifiedAt?: Date;
  id: string;
}
