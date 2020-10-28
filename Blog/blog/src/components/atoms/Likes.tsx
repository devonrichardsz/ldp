import { Button, Typography } from '@material-ui/core';
import { ThumbUp } from '@material-ui/icons';
import React from 'react';

import { useActiveUser } from 'contexts/UserContext';

export interface LikesProps {
  users: string[];
}

export const Likes: React.FC<LikesProps> = ({ users }: LikesProps) => {
  const activeUser = useActiveUser();
  const toggleLike = () => null;
  if (!activeUser) {
    return <Typography>users.length</Typography>;
  } else {
    const color = users.includes(activeUser.id) ? 'primary' : 'secondary';
    return (
      <Button startIcon={<ThumbUp color={color} />} color={color} onClick={toggleLike}>
        <Typography>{users.length}</Typography>
      </Button>
    );
  }
};

export default Likes;
