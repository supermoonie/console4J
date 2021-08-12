import React from 'react';
import {TextField, withStyles} from "@material-ui/core";
import {withSnackbar} from 'notistack';
import Container from "@material-ui/core/Container";
import Native from "@/lib/native"

const styles = theme => ({

});

class App extends React.Component {

    constructor(props) {
        super(props);
        this.state = {
            value: undefined
        }
    }

    render() {
        return <React.Fragment>
            <Container style={{width: '100%'}}>
                <TextField autoFocus={true} fullWidth={true} onChange={(e) => {
                    if (!!e.target.value) {
                        Native.jWindow.setWindowSize(800, 600).then(res => {
                            console.log(res);
                        });
                    } else {
                        Native.jWindow.setWindowSize(0, 0).then(res => {
                            console.log(res);
                        });
                    }

                }}/>
            </Container>
        </React.Fragment>
    }
}


export default withStyles(styles)(withSnackbar(App));