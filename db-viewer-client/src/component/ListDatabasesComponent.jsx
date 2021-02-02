import React, { Component } from 'react'
import DatabaseDataService from '../service/DatabaseDataService';

class ListDatabasesComponent extends Component {
    constructor(props) {
        super(props)
        this.state = {
            databases: [],
            message: null
        }
        this.deleteDatabaseClicked = this.deleteDatabaseClicked.bind(this)
        this.updateDatabaseClicked = this.updateDatabaseClicked.bind(this)
        this.addDatabaseClicked = this.addDatabaseClicked.bind(this)
        this.refreshDatabases = this.refreshDatabases.bind(this)
    }

    componentDidMount() {
        this.refreshDatabases();
    }

    refreshDatabases() {
        DatabaseDataService.retrieveAllDatabases()
            .then(
                response => {
                    //console.log(response);
                    this.setState({ databases: response.data})
                }
            )
    }

    deleteDatabaseClicked(id) {
        DatabaseDataService.deleteDatabase(id)
            .then(
                response => {
                    this.setState({ message: `Delete of database ${id} Successful` })
                    this.refreshDatabases()
                }
            )

    }

    addDatabaseClicked() {
        this.props.history.push(`/databases/-1`)
    }

    updateDatabaseClicked(id) {
        console.log('update ' + id)
        this.props.history.push(`/databases/${id}`)
    }

    getDabaseClicked(id) {
        console.log('info for: ' + id)
        this.props.history.push(`/databases/${id}/schemas`)
    }

    render() {
        console.log('render')
        return (
            <div className="container">
                <h3>All Databases</h3>
                {this.state.message && <div class="alert alert-success">{this.state.message}</div>}
                <div className="container">
                    <table className="table">
                        <thead>
                            <tr>
                                <th>Id</th>
                                <th>Name</th>
                                <th>Update</th>
                                <th>Delete</th>
                                <th>MetaData</th>
                            </tr>
                        </thead>
                        <tbody>
                            {
                                this.state.databases.map(
                                    database =>
                                        <tr key={database.id}>
                                            <td>{database.id}</td>
                                            <td>{database.name}</td>
                                            <td><button className="btn btn-success" onClick={() => this.updateDatabaseClicked(database.id)}>Update</button></td>
                                            <td><button className="btn btn-warning" onClick={() => this.deleteDatabaseClicked(database.id)}>Delete</button></td>
                                            <td><button className="btn btn-info" onClick={() => this.getDabaseClicked(database.id)}>MetaData</button></td>
                                        </tr>
                                )
                            }
                        </tbody>
                    </table>
                    <div className="row">
                        <button className="btn btn-success" onClick={this.addDatabaseClicked}>Add</button>
                    </div>
                </div>
            </div>
        )
    }
}

export default ListDatabasesComponent