import { Typography } from '@material-ui/core';
import React from 'react';

import { useActiveUser } from 'contexts/UserContext';
import { IComment } from 'types/Post';

export interface CommentProps {
  comment: IComment;
}

export const Comment: React.FC<CommentProps> = ({ comment }: CommentProps) => {
  const activeUser = useActiveUser();
  if (activeUser && comment.user.id !== activeUser.id) {
    return <Typography>comment.text</Typography>;
  } else {
    return <Typography>comment.text</Typography>;
  }
};

export default Comment;
