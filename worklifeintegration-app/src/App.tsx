import React, { createContext } from 'react';
import logo from './logo.svg';
import './App.css';
import { useInterpret } from '@xstate/react';
import { mainMachine } from './machines/main';
import NestedGrid from './components/nested-grid/NestedGrid';
import RootView from './components/root-view/RootView';
import { useMachine } from '@xstate/react';

export const GlobalStateContext = createContext({});

function App() {
  const [state, send] = useMachine(mainMachine);
  return (
    <GlobalStateContext.Provider value={{ state: state, send: send }}>
      <div className="App">
      {state.context.task_data !== null && <RootView state={state} send={send as any}/>}
    </div>
  
    </GlobalStateContext.Provider>
  );
}

export default App;
