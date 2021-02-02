import React, { Component } from 'react';
import ListDatabasesComponent from './ListDatabasesComponent';
import { BrowserRouter as Router, Route, Switch } from 'react-router-dom'
import DatabaseComponent from './DatabaseComponent';
import DatabaseMetaDataComponent from './DatabaseMetaDataComponent';

class InterviewApp extends Component {
    render() {
        return (
            <Router>
                <>
                    <h1>Interview Application</h1>
                    <Switch>
                        <Route path="/" exact component={ListDatabasesComponent} />
                        <Route path="/databases" exact component={ListDatabasesComponent} />
                        <Route path="/databases/:id" exact component={DatabaseComponent}/>
                        <Route path="/databases/:id/schemas" exact component={DatabaseMetaDataComponent} />
                    </Switch>
                </>
            </Router>
        )
    }
}
export default InterviewApp