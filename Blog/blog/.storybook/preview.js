import { ThemeProvider } from '@material-ui/core/styles';
import React from 'react';

import { UserProvider } from 'contexts/UserContext';
import { IUser } from 'types/User';
import { THEME } from 'utils/theme';

export const parameters = {
  actions: { argTypesRegex: "^on[A-Z].*" },
}

const user: IUser = {
  id: '3456',
  name: 'Test User',
  following: ['4567'],
};

export const decorators = [
  (Story) => (
    <ThemeProvider theme={THEME}>
      <UserProvider value={user}>
        <Story />
      </UserProvider>
    </ThemeProvider>
  ),
];
