import React, { Component } from 'react'
import { Formik, Form, Field, ErrorMessage } from 'formik';
import DatabaseDataService from '../service/DatabaseDataService';

class DatabaseComponent extends Component {
    constructor(props) {
        super(props)

        this.state = {
            id: this.props.match.params.id,
            name: null,
            hostname: null,
            port: null,
            databaseName: null,
            username: null,
            password: null
        }

        this.onSubmit = this.onSubmit.bind(this)
        this.validate = this.validate.bind(this)

    }

    componentDidMount() {

        console.log(this.state.id)

        // eslint-disable-next-line
        if (this.state.id == -1) {
            return
        }

        DatabaseDataService.retrieveDatabase(this.state.id)
            .then(response => this.setState({
                name: response.data.name,
                hostname: response.data.hostname,
                port: response.data.port,
                databaseName: response.data.databaseName,
                username: response.data.username,
                password: response.data.password
            }))
    }

    validate(values) {
        console.log('validate');
        let errors = {}
        if (!values.name) {
            console.log('validateif');
            errors.name = 'Enter a Name'
        }
        console.log('validate2');
        return errors;

    }

    onSubmit(values) {
        console.log('this state id: ' + this.state.id);
        let database = {
            id: this.state.id,
            name: values.name,
            hostname: values.hostname,
            port: values.port,
            databaseName: values.databaseName,
            username: values.username,
            password: values.password
        }
        console.log('this state id again: ' + this.state.id);
        if (this.state.id === '-1') {
            console.log('onsubmit create');
            DatabaseDataService.createDatabase(database)
                .then(() => this.props.history.push('/databases'))
        } else {
            console.log('onsubmit update');
            DatabaseDataService.updateDatabase(this.state.id, database)
                .then(() => this.props.history.push('/databases'))
        }

        console.log(values);
    }

    render() {

        let { id, name, hostname, port, databaseName, username, password } = this.state

        return (
            <div>
                <h3>Database</h3>
                <div className="container">
                    <Formik
                        initialValues={{ id, name, hostname, port, databaseName, username, password }}
                        onSubmit={this.onSubmit}
                        validateOnChange={false}
                        validateOnBlur={false}
                        validate={this.validate}
                        enableReinitialize={true}
                    >
                        {
                            (props) => (
                                <Form>
                                    <ErrorMessage name="name" component="div"
                                        className="alert alert-warning" />
                                    <fieldset className="form-group">
                                        <label>Id (auto generated with new entry)</label>
                                        <Field className="form-control" type="text" name="id" disabled />
                                    </fieldset>
                                    <fieldset className="form-group">
                                        <label>Name</label>
                                        <Field className="form-control" type="text" name="name" />
                                    </fieldset>
                                    <fieldset className="form-group">
                                        <label>HostName</label>
                                        <Field className="form-control" type="text" name="hostname" />
                                    </fieldset>
                                    <fieldset className="form-group">
                                        <label>Port (number)</label>
                                        <Field className="form-control" type="text" name="port" />
                                    </fieldset>
                                    <fieldset className="form-group">
                                        <label>DatabaseName</label>
                                        <Field className="form-control" type="text" name="databaseName" />
                                    </fieldset>
                                    <fieldset className="form-group">
                                        <label>UserName</label>
                                        <Field className="form-control" type="text" name="username" />
                                    </fieldset>
                                    <fieldset className="form-group">
                                        <label>Password</label>
                                        <Field className="form-control" type="text" name="password" />
                                    </fieldset>
                                    <button className="btn btn-success" type="submit">Save</button>
                                </Form>
                            )
                        }
                    </Formik>

                </div>
            </div>
        )
    }
}

export default DatabaseComponent