import { Typography } from '@material-ui/core';
import React from 'react';

import { useActiveUser } from 'contexts/UserContext';
import { useUserById } from 'services/UserService';
import { IComment } from 'types/Post';

export interface CommentProps {
  comment: IComment;
}

export const Comment: React.FC<CommentProps> = ({ comment }: CommentProps) => {
  const activeUser = useActiveUser();
  const author = useUserById(comment.author);
  if (activeUser && author && author.id !== activeUser.id) {
    return <Typography>comment.text</Typography>;
  } else {
    return <Typography>comment.text</Typography>;
  }
};

export default Comment;
