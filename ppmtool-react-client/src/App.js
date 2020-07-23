import React, { Component } from 'react';
import './App.css';
import Header from './components/Layout/Header';
import Dashboard from './components/Dashboard';
import AddProject from './components/Project/AddProject';
import UpdateProject from './components/Project/UpdateProject';
import "bootstrap/dist/css/bootstrap.min.css";
import { BrowserRouter as Router, Route } from 'react-router-dom';
import { Provider } from 'react-redux';
import store from "./store";

class App extends Component {
  render() {
    return (
      <Provider store={store}>
        <Router>
          <div className="App">
            <Header />
            <Route exact path="/dashboard" component={Dashboard} />
            <Route exact path="/addProject" component={AddProject} />
            <Route exact path="/updateProject/:id" component={UpdateProject} />
          </div>
        </Router>
      </Provider>
    );
  }

}

export default App;
