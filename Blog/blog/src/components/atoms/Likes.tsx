import { Button, Typography } from '@material-ui/core';
import React from 'react';

import { useActiveUser } from 'contexts/UserContext';

export interface LikesProps {
  users: [string];
}

export const Likes: React.FC<LikesProps> = ({ users }: LikesProps) => {
  const activeUser = useActiveUser();
  const toggleLike = () => null;
  if (!activeUser) {
    return <Typography>users.length</Typography>;
  } else {
    return (
      <Button color={users.includes(activeUser.id) ? 'secondary' : 'primary'} onClick={toggleLike}>
        <Typography>{users.length}</Typography>
      </Button>
    );
  }
};

export default Likes;
