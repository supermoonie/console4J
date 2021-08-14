import React from "react";
import {withStyles} from "@material-ui/core";
import List from "@material-ui/core/List";
import ListItem from "@material-ui/core/ListItem";
import ListItemText from "@material-ui/core/ListItemText";
import Jvm from "@/lib/Jvm";
import ListItemSecondaryAction from "@material-ui/core/ListItemSecondaryAction";
import Divider from "@material-ui/core/Divider";
import SendIcon from '@material-ui/icons/Send';
import Button from "@material-ui/core/Button";
import ListSubheader from "@material-ui/core/ListSubheader";
import TextField from "@material-ui/core/TextField";

const styles = theme => ({});

class NewConnection extends React.Component {

    constructor(props) {
        super(props);
        this.state = {
            localJvmList: []
        }
    }

    componentDidMount() {
        Jvm.getLocalJvm().then(vms => {
            this.setState({
                localJvmList: vms
            })
        })
    }

    render() {
        const classes = this.props.classes;
        return <React.Fragment>
            <List dense={false}
                  subheader={
                      <div>
                          <ListSubheader component="div" id="local-list-subheader">
                              Local Process:
                          </ListSubheader>
                          <Divider/>
                      </div>
                  }
            >
                {
                    this.state.localJvmList.map((item) => (
                        <div key={item.pid}>
                            <ListItem>
                                <ListItemText>
                                    {
                                        item.pid + ':' + item.name
                                    }
                                </ListItemText>
                                <ListItemSecondaryAction>
                                    <Button
                                        variant="contained"
                                        color="primary"
                                        className={classes.button}
                                        endIcon={<SendIcon/>}
                                    >
                                        connect
                                    </Button>
                                </ListItemSecondaryAction>
                            </ListItem>
                            <Divider/>
                        </div>
                    ))
                }
            </List>
            <List dense={false}
                  subheader={
                      <div>
                          <ListSubheader component="div" id="remote-list-subheader">
                              Remote Process:
                          </ListSubheader>
                          <Divider/>
                      </div>
                  }
            >
                <div key={"remote"}>
                    <ListItem>
                        <ListItemText>
                            <TextField style={{width: '80%'}} size={"small"} id="remote-host-port" label="<hostname>:<port>"/>
                        </ListItemText>
                        <ListItemSecondaryAction>
                            <Button
                                variant="contained"
                                color="primary"
                                className={classes.button}
                                endIcon={<SendIcon/>}
                            >
                                connect
                            </Button>
                        </ListItemSecondaryAction>
                    </ListItem>
                    <Divider/>
                </div>
            </List>
        </React.Fragment>
    }
}

export default withStyles(styles)(NewConnection);