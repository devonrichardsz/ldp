import blueGrey from '@material-ui/core/colors/blueGrey';
import indigo from '@material-ui/core/colors/indigo';
import { createMuiTheme } from '@material-ui/core/styles';

export const THEME = createMuiTheme({
  typography: { fontFamily: 'Roboto' },
  palette: {
    primary: indigo,
    secondary: blueGrey,
  },
});

export default THEME;
