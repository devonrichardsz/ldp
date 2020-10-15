import React from 'react';

import { Markdown } from 'components/atoms/Markdown';
import { IPost } from 'types/Post';

export interface PostProps {
  post: IPost;
}

export const Post: React.FC<PostProps> = ({ post }: PostProps) => <Markdown text={post.body} />;

export default Post;
