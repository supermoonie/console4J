import React from "react";
import {withStyles} from "@material-ui/core";
import List from "@material-ui/core/List";
import ListItem from "@material-ui/core/ListItem";
import ListItemText from "@material-ui/core/ListItemText";
import Jvm from "@/lib/Jvm";

const styles = theme => ({

});

class NewConnection extends React.Component {

    constructor(props) {
        super(props);
        this.state = {
            localJvms: []
        }
    }

    componentDidMount() {
        Jvm.getLocalJvm().then(vms => {
            console.log(vms);
        })
    }

    render() {
        const classes = this.props.classes;
        return <React.Fragment>
            <List dense={false}>
                {
                    this.state.localJvms.forEach((item, index) => (
                        <ListItem key={item.pid}>
                            <ListItemText>
                                {
                                    item.pid + ':' + item.name
                                }
                            </ListItemText>
                        </ListItem>
                    ))
                }
            </List>
        </React.Fragment>
    }
}

export default withStyles(styles)(NewConnection);