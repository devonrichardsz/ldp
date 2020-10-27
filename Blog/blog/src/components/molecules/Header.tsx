import { AppBar, Toolbar, Typography } from '@material-ui/core';
import React from 'react';

import { useActiveUser } from 'contexts/UserContext';
import { IUser } from 'types/User';

export interface HeaderProps {
  pageUser?: IUser;
}

export const Header: React.FC<HeaderProps> = ({ pageUser }: HeaderProps) => {
  const activeUser = useActiveUser();
  return (
    <AppBar position="sticky">
      <Toolbar>
        <Typography>{pageUser?.name}</Typography>
        <Typography>{activeUser?.name}</Typography>
      </Toolbar>
    </AppBar>
  );
};

export default Header;
