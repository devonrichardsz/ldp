import { Button, Typography } from '@material-ui/core';
import React from 'react';

import { useActiveUser } from 'contexts/UserContext';
import { IUser } from 'types/User';

export interface FollowButtonProps {
  toFollow: IUser;
}

export const FollowButton: React.FC<FollowButtonProps> = ({ toFollow }: FollowButtonProps) => {
  const activeUser = useActiveUser();
  if (!activeUser || activeUser.id === toFollow.id) {
    return null;
  } else {
    const toggleFollowing = () => null;
    return (
      <Button color="primary" onClick={toggleFollowing}>
        <Typography>
          {activeUser.following.includes(toFollow.id) ? 'Unfollow' : 'Follow'}
        </Typography>
      </Button>
    );
  }
};
