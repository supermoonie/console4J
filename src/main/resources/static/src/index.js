import React from 'react';
import ReactDOM from 'react-dom';
import Theme from '@/components/Theme';
import {Card, CardContent, CardMedia, Divider, Link, TextField, ThemeProvider} from "@material-ui/core";
import CssBaseline from "@material-ui/core/CssBaseline";
import {SnackbarProvider} from "notistack";
import Container from "@material-ui/core/Container";
import Grid from "@material-ui/core/Grid";
import Typography from "@material-ui/core/Typography";
import Box from "@material-ui/core/Box";
import App from "@/App"

ReactDOM.render(
    <ThemeProvider theme={Theme}>
        <SnackbarProvider maxSnack={3}>
            <CssBaseline/>
            <App />
        </SnackbarProvider>
    </ThemeProvider>, document.getElementById('app')
);