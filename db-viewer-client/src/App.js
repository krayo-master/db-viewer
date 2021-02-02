import React, { Component } from 'react';
import './App.css';
import DatabaseApp from './component/InterviewApp';

class App extends Component {
  render() {
    return (
      <div className="container">
        <DatabaseApp />
      </div>
    );
  }
}

export default App;
