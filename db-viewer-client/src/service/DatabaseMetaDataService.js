import axios from 'axios'

const BASE_API_URL = 'http://localhost:8080/api'

class DatabaseMetaDataService {

    retrieveAllSchemas(id) {
        console.log('executed service retrieveAllSchemas: ' + id)
        return axios.get(`${BASE_API_URL}/databases/${id}/schemas`);
    }

    retrieveAllTables(id, schemaName) {
        console.log('executed service retrieveAllTables')
        return axios.get(`${BASE_API_URL}/databases/${id}/schemas/${schemaName}/tables`);
    }

    retrieveAllColumns(id, schemaName, tableName) {
        console.log('executed service retrieveAllColumns')
        return axios.get(`${BASE_API_URL}/databases/${id}/schemas/${schemaName}/tables/${tableName}`);

    }

    previewTable(id, schemaName, tableName) {
        console.log('executed service previewTable')
        return axios.get(`${BASE_API_URL}/databases/${id}/schemas/${schemaName}/tables/${tableName}/preview`);
    }

    retrieveTableStats(id, schemaName, tableName) {
        console.log('executed service retrieveTableStats')
        return axios.get(`${BASE_API_URL}/databases/${id}/schemas/${schemaName}/tables/${tableName}/stats`);
    }

    retrieveColumnStats(id, schemaName, tableName, columnName) {
        console.log('executed service retrieveColumnStats')
        return axios.get(`${BASE_API_URL}/databases/${id}/schemas/${schemaName}/tables/${tableName}/columns/${columnName}/stats`);
    }

}

export default new DatabaseMetaDataService()