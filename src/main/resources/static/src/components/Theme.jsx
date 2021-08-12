import { createTheme } from '@material-ui/core/styles';

const theme = createTheme({
    typography: {
        button: {
            textTransform: 'none'
        }
    },
    app: {
        backgroundColor: '#ccc'
    },
});

export default theme;