import { ThemeProvider } from '@material-ui/core/styles';
import React from 'react';

import { UserProvider } from 'contexts/UserContext';
import { THEME } from 'App';

export const parameters = {
  actions: { argTypesRegex: "^on[A-Z].*" },
}

const user = {
  id: '3265',
  name: 'Test User',
  following: [],
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
