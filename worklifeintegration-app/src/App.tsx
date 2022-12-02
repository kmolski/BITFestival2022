import React, { createContext } from 'react';
import logo from './logo.svg';
import './App.css';
import { useInterpret } from '@xstate/react';
import { mainMachine } from './machines/main';

export const GlobalStateContext = createContext({});

function App() {
  const mainService = useInterpret(mainMachine);
  
  return (
    <GlobalStateContext.Provider value={{ mainService }}>
      <div className="App">
      <header className="App-header">
        <img src={logo} className="App-logo" alt="logo" />
        <p>
          Edit <code>src/App.tsx</code> and save to reload.
        </p>
        <a
          className="App-link"
          href="https://reactjs.org"
          target="_blank"
          rel="noopener noreferrer"
        >
          Learn React
        </a>
      </header>
    </div>
    </GlobalStateContext.Provider>
  );
}

export default App;
