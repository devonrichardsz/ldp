import { ThemeProvider } from '@material-ui/core/styles';
import React, { useEffect, useState } from 'react';
import { BrowserRouter as Router, Route, Switch } from 'react-router-dom';

import { Header } from 'components/molecules/Header';
import { UserProvider } from 'contexts/UserContext';
import { IUser } from 'types/User';
import THEME from 'utils/theme';

export const App: React.FC = () => {
  const [user, setUser] = useState<IUser | null>(null);
  useEffect(() => {
    // TODO: Load user from server.
    setUser({
      id: '3265',
      name: 'Test User',
      following: [],
    });
  }, []);
  return (
    <ThemeProvider theme={THEME}>
      <UserProvider value={user}>
        <Header />
        <Router>
          <Switch>
            <Route path="/" />
          </Switch>
        </Router>
      </UserProvider>
    </ThemeProvider>
  );
};

export default App;
