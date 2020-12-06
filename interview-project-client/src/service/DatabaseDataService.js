import axios from 'axios'

const BASE_API_URL = 'http://localhost:8080/api'

class DatabaseDataService {

    retrieveAllDatabases() {
        console.log('executed service retrieveAllDatabases')
        return axios.get(`${BASE_API_URL}/databases`);
    }

    retrieveDatabase(id) {
        //console.log('executed service')
        return axios.get(`${BASE_API_URL}/databases/${id}`);
    }

    deleteDatabase(id) {
        //console.log('executed service')
        return axios.delete(`${BASE_API_URL}/databases/${id}`);
    }

    updateDatabase(id, database) {
        //console.log('executed service')
        return axios.put(`${BASE_API_URL}/databases/${id}`, database);
    }

    createDatabase(database) {
        console.log('executed service')
        return axios.post(`${BASE_API_URL}/databases`, database);
    }
}

export default new DatabaseDataService()