import React from 'react';

import { Post } from 'components/molecules/Post';
import { IPost } from 'types/Post';

export interface PostsListsProps {
  posts: [IPost];
}

export const PostsList: React.FC<PostsListsProps> = ({ posts }: PostsListsProps) => (
  <>
    {posts.map((post) => (
      <Post key={post.id} post={post} />
    ))}
  </>
);

export default PostsList;
