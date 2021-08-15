import React from 'react';
import {ThemeProvider, withStyles} from "@material-ui/core";
import SpeedDialAction from '@material-ui/lab/SpeedDialAction';
import {SnackbarProvider} from 'notistack';
import Container from "@material-ui/core/Container";
import LightTheme from "@/components/LightTheme";
import DarkTheme from "@/components/DarkTheme";
import CssBaseline from "@material-ui/core/CssBaseline";
import {SpeedDial, SpeedDialIcon} from "@material-ui/lab";
import Brightness4Icon from '@material-ui/icons/Brightness4';
import Brightness7Icon from '@material-ui/icons/Brightness7';
import AddIcon from '@material-ui/icons/Add';
import EditIcon from '@material-ui/icons/Edit';
import Theme from "@/lib/Theme";
import NewConnection from "@/components/NewConnection";
import Drawer from "@material-ui/core/Drawer";
import AppBar from "@material-ui/core/AppBar";
import Tabs from "@material-ui/core/Tabs";
import Tab from "@material-ui/core/Tab";
import TabPanel from "@/components/TabPanel";
import ReactECharts from 'echarts-for-react';
import * as echarts from 'echarts';

echarts.registerTheme('dark', {
    label: '#fafafa'
});

const styles = theme => ({
    speedDial: {
        position: 'absolute',
        bottom: theme.spacing(8),
        right: theme.spacing(8),
    },
    container: {
        width: '100%',
        maxWidth: '100%',
        margin: 0,
        padding: 0
    }
});

class App extends React.Component {

    constructor(props) {
        super(props);
        this.state = {
            value: undefined,
            dark: true,
            openSpeedDial: false,
            showNewConnection: false,
            activeTab: 0,
            testOption: {
                title: {
                    text: '堆叠区域图'
                },
                tooltip : {
                    trigger: 'axis'
                },
                labelLine: {
                    lineStyle: {
                        color: 'rgba(255, 255, 255, 0.3)'
                    }
                },
                label: {
                    textStyle: {
                        color: 'rgba(255, 255, 255, 0.3)'
                    }
                },
                legend: {
                    data:['邮件营销','联盟广告','视频广告']
                },
                toolbox: {
                    feature: {
                        saveAsImage: {}
                    }
                },
                grid: {
                    left: '3%',
                    right: '4%',
                    bottom: '3%',
                    containLabel: true
                },
                xAxis : [
                    {
                        type : 'category',
                        boundaryGap : false,
                        data : ['周一','周二','周三','周四','周五','周六','周日']
                    }
                ],
                yAxis : [
                    {
                        type : 'value'
                    }
                ],
                series : [
                    {
                        name:'邮件营销',
                        type:'line',
                        stack: '总量',
                        areaStyle: {normal: {}},
                        data:[120, 132, 101, 134, 90, 230, 210]
                    },
                    {
                        name:'联盟广告',
                        type:'line',
                        stack: '总量',
                        areaStyle: {normal: {}},
                        data:[220, 182, 191, 234, 290, 330, 310]
                    },
                    {
                        name:'视频广告',
                        type:'line',
                        stack: '总量',
                        areaStyle: {normal: {}},
                        data:[150, 232, 201, 154, 190, 330, 410]
                    }
                ]
            }
        }
    }

    componentDidMount() {
        Theme.getTheme().then(theme => {
            this.setState({
                dark: "dark" === theme
            })
        })
    }

    render() {
        const classes = this.props.classes;
        return <ThemeProvider theme={this.state.dark ? DarkTheme : LightTheme}>
            <SnackbarProvider maxSnack={3}>
                <CssBaseline/>
                <Container className={classes.container}>
                    <AppBar position={"static"}>
                        <Tabs value={this.state.activeTab} onChange={(event, value) => {
                            console.log(value);
                            this.setState({
                                activeTab: value
                            })
                        }}>
                            <Tab label={"tab-0"} id={"tab-0"} aria-details={"tab-panel-0"} />
                            <Tab label={"tab-1"} id={"tab-1"} aria-details={"tab-panel-1"} />
                        </Tabs>
                    </AppBar>
                    <TabPanel value={this.state.activeTab} index={0}>
                        <ReactECharts
                            option={this.state.testOption}
                            style={{ height: 400 }}
                        />
                    </TabPanel>
                    <TabPanel value={this.state.activeTab} index={1}>
                        Item Two
                    </TabPanel>
                    <Drawer anchor="top" open={this.state.showNewConnection} onClose={() => {
                        this.setState({
                            showNewConnection: false
                        })
                    }}>
                        <NewConnection/>
                    </Drawer>
                    <SpeedDial
                        ariaLabel=""
                        className={classes.speedDial}
                        hidden={false}
                        icon={<SpeedDialIcon openIcon={<EditIcon/>}/>}
                        onClose={() => {
                            this.setState({
                                openSpeedDial: false
                            })
                        }}
                        onOpen={() => {
                            this.setState({
                                openSpeedDial: true
                            })
                        }}
                        open={this.state.openSpeedDial}
                    >
                        <SpeedDialAction
                            key={"newConnection"}
                            icon={<AddIcon/>}
                            tooltipTitle={""}
                            title={""}
                            onClick={() => {
                                this.setState({
                                    showNewConnection: true,
                                    openSpeedDial: false
                                })
                            }}
                        />
                        <SpeedDialAction
                            key={"switchTheme"}
                            title={""}
                            icon={this.state.dark ? <Brightness7Icon/> : <Brightness4Icon/>}
                            tooltipTitle={""}
                            onClick={() => {
                                Theme.setTheme(this.state.dark ? 'light' : 'dark').then(res => {
                                    this.setState({
                                        dark: !this.state.dark,
                                        openSpeedDial: false
                                    })
                                })
                            }}
                        />
                    </SpeedDial>
                </Container>
            </SnackbarProvider>
        </ThemeProvider>
    }
}


export default withStyles(styles)(App);