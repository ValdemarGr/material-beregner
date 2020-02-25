import 'date-fns';
import React from 'react';
import Button from '@material-ui/core/Button';
import CssBaseline from '@material-ui/core/CssBaseline';
import TextField from '@material-ui/core/TextField';
import FormControlLabel from '@material-ui/core/FormControlLabel';
import Checkbox from '@material-ui/core/Checkbox';
import Link from '@material-ui/core/Link';
import Grid from '@material-ui/core/Grid';
import Container from '@material-ui/core/Container';
import {Link as RouterLink, Redirect} from 'react-router-dom';
import {Box, createStyles, Theme, WithStyles} from "@material-ui/core";
import Card from "@material-ui/core/Card";
import {Chart} from "react-google-charts";
import Typography from "@material-ui/core/Typography";
import {withStyles} from "@material-ui/styles";
import TableBody from "@material-ui/core/TableBody";
import Table from "@material-ui/core/Table";
import TableRow from "@material-ui/core/TableRow";
import TableCell from "@material-ui/core/TableCell";
import {
    MuiPickersUtilsProvider,
    KeyboardTimePicker,
    KeyboardDatePicker,
} from '@material-ui/pickers';
import DateFnsUtils from '@date-io/date-fns';
import Fade from "@material-ui/core/Fade";
import Paper from "@material-ui/core/Paper";
import IconButton from "@material-ui/core/IconButton";
import CloseIcon from '@material-ui/icons/Close';
import {Publish, WebsocketObservable} from "./WebsocketManager";
import {Observable} from "rxjs";

const styles = (theme: Theme) => createStyles({
    root: {
        flexGrow: 1
    },
    card: {
        paddingBottom: 50
    },
    title: {
        textAlign: "center",
        marginTop: 20,
        marginBottom: 5
    },
    paper: {
    }
});

function dollarTypeDecoder(obj: any): ServerResponse {
    const tpe: string = obj.$type;
    if (tpe === 'UpdateSumResponse') {
        const r: UpdateSumResponse = {
            v: obj.v
        };

        return r;
    }

    return null as unknown as ServerResponse;
}

interface ServerResponse {}

interface UpdateSumResponse extends ServerResponse {
    v: number
}

interface UpdateSumRequest {
    $type: 'UpdateSumRequest',
    fields: Array<number>
}

interface AppProps extends WithStyles<typeof styles> {

}

interface AppState {
    value1: number,
    value2: number,
    value3: number,
    apiSum: number
}

class AppC extends React.Component<AppProps, AppState> {
    constructor(props: AppProps) {
        super(props);

        this.obs.forEach((value: UpdateSumResponse) => {
            this.setState({apiSum: value.v});
        });

        this.state = {
            value1: 0,
            value2: 0,
            value3: 0,
            apiSum: 0
        };
    }

    obs: Observable<UpdateSumResponse> = WebsocketObservable<UpdateSumResponse>();

    public requestSumUpdate() {
        const request: UpdateSumRequest = {
          $type: "UpdateSumRequest",
          fields: [this.state.value1, this.state.value2, this.state.value3]
        };

        Publish<UpdateSumRequest>(request);
    }

    handleChange = (name: keyof AppState) => (event: React.ChangeEvent<HTMLInputElement>) => {
        this.setState({ ...this.state, [name]: event.target.value });
    };

    public render() {
        return (
            <div className={this.props.classes.root}>
                <Grid container spacing={5}>
                    <Grid item xs={6}>
                        <Card className={this.props.classes.card}>
                            <Typography
                                variant={"h4"}
                                className={this.props.classes.title}
                            >
                                Input
                            </Typography>
                            <Container maxWidth={"lg"}>
                                <TextField
                                    id="standard-number"
                                    label="Number1"
                                    value={this.state.value1}
                                    onChange={this.handleChange('value1')}
                                    type="number"
                                    InputLabelProps={{
                                        shrink: true,
                                    }}
                                    margin="normal"
                                />
                                <br/>
                                <TextField
                                    id="standard-number"
                                    label="Number2"
                                    value={this.state.value2}
                                    onChange={this.handleChange('value2')}
                                    type="number"
                                    InputLabelProps={{
                                        shrink: true,
                                    }}
                                    margin="normal"
                                />
                                <br/>
                                <TextField
                                    id="standard-number"
                                    label="Number3"
                                    value={this.state.value3}
                                    onChange={this.handleChange('value3')}
                                    type="number"
                                    InputLabelProps={{
                                        shrink: true,
                                    }}
                                    margin="normal"
                                />
                                <br/>
                                <Button variant="contained" color="primary" onClick={(e) => this.requestSumUpdate()}>
                                    Submit
                                </Button>
                            </Container>
                        </Card>
                    </Grid>
                    <Grid item xs={6}>
                        <Card className={this.props.classes.card}>
                            <Typography
                                variant={"h4"}
                                className={this.props.classes.title}
                            >
                                Output
                            </Typography>
                            <Container maxWidth={"lg"}>
                                {this.state.apiSum}
                            </Container>
                        </Card>
                    </Grid>
                    <Grid item xs={6} />
                </Grid>
            </div>
        );
    }
}

const App = withStyles(styles)(AppC);

export default App;
