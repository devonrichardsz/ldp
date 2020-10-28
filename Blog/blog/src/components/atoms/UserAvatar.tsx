import { Avatar } from '@material-ui/core';
import React from 'react';

import { IUser } from 'types/User';

export interface UserAvatarProps {
  user: IUser | null;
}

export const UserAvatar: React.FC<UserAvatarProps> = ({ user }: UserAvatarProps) => {
  if (!user) {
    return <></>;
  } else if (user.avatarUrl) {
    return <Avatar alt={user.name} src={user.avatarUrl} />;
  } else {
    return <Avatar alt={user.name}>{user.name[0]}</Avatar>;
  }
};

export default UserAvatar;
