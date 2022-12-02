import React, { createContext } from 'react';
import logo from './logo.svg';
import './App.css';
import { useInterpret } from '@xstate/react';
import { mainMachine } from './machines/main';
import Column from './components/column/Column';

export const GlobalStateContext = createContext({});

function App() {
  const mainService = useInterpret(mainMachine);
  
  return (
    <GlobalStateContext.Provider value={{ mainService }}>
      <div className="App">
      <Column/>
    </div>
    </GlobalStateContext.Provider>
  );
}

export default App;
