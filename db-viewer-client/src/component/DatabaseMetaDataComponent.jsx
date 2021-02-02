import React, { Component } from 'react'
import DatabaseMetaDataService from '../service/DatabaseMetaDataService';

class DatabaseMetaDataComponent extends Component {
    constructor(props) {
        super(props)

        this.state = {
            id: this.props.match.params.id,
            schemaNames: [],
            currentSchemaName: null,
            tableNames: [],
            currentTableName: null,
            columnNames: [],
            primaryKey: [],
            foreignKeys: [],
            previewTable: [{ columnName: '' }, { columnName: '' }],
            tableStats: {
                numberOfRecords: 0,
                numberOfAttributes: 0
            },
            columnStats: {
                min: 0,
                max: 0,
                median: 0,
                avg: 0
            },
            message: null
        }

        this.onClickSchema = this.onClickSchema.bind(this)
        this.onClickTable = this.onClickTable.bind(this)
        this.refreshSchemas = this.refreshSchemas.bind(this)
        this.onPreviewTable = this.onPreviewTable.bind(this)
        this.onTableStats = this.onTableStats.bind(this)
        this.onColumnStats = this.onColumnStats.bind(this)
    }

    componentDidMount() {
        console.log('metadatacomponent here');
        this.refreshSchemas(this.state.id);
    }

    refreshSchemas(id) {
        console.log('refreshschemas ehre');
        DatabaseMetaDataService.retrieveAllSchemas(id)
            .then(
                response => {
                    console.log('response: ' + response.data);
                    this.setState({ schemaNames: response.data })
                }
            )
    }

    onClickSchema(schemaName) {
        console.log('this schema name opened: ' + schemaName);
        this.setState({ currentSchemaName: schemaName });
        toggle('tables','block');
        DatabaseMetaDataService.retrieveAllTables(this.state.id, schemaName)
            .then(
                response => {
                    //console.log(response);
                    this.setState({ tableNames: response.data })
                }
            )
    }

    onClickTable(tableName) {
        console.log('this table name opened: ' + tableName);
        this.setState({ currentTableName: tableName });
        toggle('columns','block');
        toggle('tablePreview','none');
        toggle('tableStats','none');
        DatabaseMetaDataService.retrieveAllColumns(this.state.id, this.state.currentSchemaName, tableName)
            .then(
                response => {
                    this.setState({ columnNames: response.data.columnList })
                    this.setState({ primaryKey: response.data.primaryKey })
                    this.setState({ foreignKeys: response.data.foreignKeys })
                    console.log('primary keys; ' + response.data.columnList)
                    console.log('primary keys; ' + response.data.primaryKey)
                    console.log('primary keys; ' + response.data.foreignKeys)
                }
            )
    }

    onPreviewTable(tableName) {
        console.log('this table name previewed: ' + tableName);
        this.setState({ currentTableName: tableName });
        toggle('columns','none');
        toggle('tablePreview','block');
        toggle('tableStats','none');
        DatabaseMetaDataService.previewTable(this.state.id, this.state.currentSchemaName, tableName)
            .then(
                response => {
                    console.log('preview: ' + JSON.stringify(response.data));
                    this.setState({ previewTable: response.data })
                    Object.entries(this.state.previewTable[0]).map(([key, value]) => {
                        console.log('key: ' + key);
                        console.log('value: ' + value);
                    })
                }
            )
    }

    onTableStats(tableName) {
        console.log('this table stats opened: ' + tableName);
        this.setState({ currentTableName: tableName });
        toggle('columns','none');
        toggle('tablePreview','none');
        toggle('tableStats','block');
        DatabaseMetaDataService.retrieveTableStats(this.state.id, this.state.currentSchemaName, tableName)
            .then(
                response => {
                    this.setState({ tableStats: response.data })
                }
            )
    }

    onColumnStats(columnName) {
        console.log('this column stats opened: ' + columnName);
        toggle('columnStats','block');
        DatabaseMetaDataService.retrieveColumnStats(this.state.id, this.state.currentSchemaName, this.state.currentTableName, columnName)
            .then(
                response => {
                    this.setState({ columnStats: response.data })
                }
            )
    }

    render() {
        console.log('render')
        return (
            <div className="container">
                <h3>MetaData for database id: {this.state.id}</h3>
                {this.state.message && <div class="alert alert-success">{this.state.message}</div>}
                <div className="container">
                    <table className="table">
                        {/* schemas */}
                        <thead>
                            <tr>
                                <th>SchemaName</th>
                            </tr>
                        </thead>
                        <tbody>
                            {
                                this.state.schemaNames.map(
                                    schema =>
                                        <tr key={schema}>
                                            <td><button className="btn btn-info" onClick={() => this.onClickSchema(schema)}>{schema}</button></td>
                                        </tr>
                                )
                            }
                        </tbody>
                        {/* tables */}
                        <thead className="tables" style={{display: 'none' }}>
                            <tr>
                                <th>TableName</th>
                                <th>Preview</th>
                                <th>TableStats</th>
                            </tr>
                        </thead>
                        <tbody className="tables" style={{display: 'none' }}>
                            {
                                this.state.tableNames.map(
                                    table =>
                                        <tr key={table}>
                                            <td><button className="btn btn-success" onClick={() => this.onClickTable(table)}>{table}</button></td>
                                            <td><button className="btn btn-primary" onClick={() => this.onPreviewTable(table)}>Preview</button></td>
                                            <td><button className="btn btn-info" onClick={() => this.onTableStats(table)}>Stats</button></td>
                                        </tr>
                                )
                            }
                        </tbody>
                        {/* columns */}
                        <thead className="columns" style={{display: 'none' }}>
                            <tr>
                                <th>ColumnName</th>
                                <th>Size</th>
                                <th>DataType</th>
                                <th>IsNullable</th>
                                <th>IsAutoIncrement</th>
                                <th>ColumnStats</th>
                            </tr>
                        </thead>
                        <tbody className="columns" style={{display: 'none' }}>
                            <tr>Primary Key: {this.state.primaryKey}</tr>
                            <tr>Foreign Keys: {this.state.foreignKeys}</tr>
                            {
                                this.state.columnNames.map(
                                    column =>
                                        <tr key={column.name}>
                                            <td>{column.name}</td>
                                            <td>{column.size}</td>
                                            <td>{column.datatype}</td>
                                            <td>{column.isNullable}</td>
                                            <td>{column.isAutoIncrement}</td>
                                            <td><button className="btn btn-info" onClick={() => this.onColumnStats(column.name)}>Stats</button></td>
                                        </tr>
                                )
                            }
                        </tbody>
                        {/* tablePreview */}
                        <thead className="tablePreview" style={{display: 'none' }}>
                            <tr>
                                {
                                    Object.entries(this.state.previewTable[0]).map(([key, value]) => {
                                        return (<th>{key}</th>)
                                    })
                                }
                            </tr>
                        </thead>
                        <tbody className="tablePreview" style={{display: 'none' }}>
                            {
                                this.state.previewTable.map(
                                    entry => {
                                        return (<tr>
                                            {Object.entries(entry).map(([key, value]) => {
                                                return (<td>{value}</td>)
                                            })}
                                        </tr>)
                                    }
                                )}
                        </tbody>
                        {/* tableStats */}
                        <thead className="tableStats" style={{display: 'none' }}>
                            <tr>
                                <th>NumberOfRecords</th>
                                <th>NumberOfAttributes</th>
                            </tr>
                        </thead>
                        <tbody className="tableStats" style={{display: 'none' }}>
                            {
                                <tr key={this.state.tableStats.numberOfRecords}>
                                    <td>{this.state.tableStats.numberOfRecords}</td>
                                    <td>{this.state.tableStats.numberOfAttributes}</td>
                                </tr>
                            }
                        </tbody>
                        {/* columnStats */}
                        <thead className="columnStats" style={{display: 'none' }}>
                            <tr>
                                <th>Min</th>
                                <th>Max</th>
                                <th>Median</th>
                                <th>Avg</th>
                            </tr>
                        </thead>
                        <tbody className="columnStats" style={{display: 'none' }}>
                            {
                                <tr key={this.state.columnStats.min}>
                                    <td>{this.state.columnStats.min}</td>
                                    <td>{this.state.columnStats.max}</td>
                                    <td>{this.state.columnStats.median}</td>
                                    <td>{this.state.columnStats.avg}</td>
                                </tr>
                            }
                        </tbody>
                    </table>
                </div>
            </div>
        )
    }
}

function toggle(className, displayState){
    var elements = document.getElementsByClassName(className)
    for (var i = 0; i < elements.length; i++){
        elements[i].style.display = displayState;
    }
}

export default DatabaseMetaDataComponent