import { AnyEventObject, BaseActionObject, EventData, ResolveTypegenMeta, SCXML, ServiceMap, SingleOrArray, State, TypegenDisabled } from "xstate";
import { MainContext } from "./main";

export type stateType = State<MainContext, AnyEventObject, any, {
    value: any;
    context: MainContext;
}, ResolveTypegenMeta<TypegenDisabled, AnyEventObject, BaseActionObject, ServiceMap>>

export type stateSend = (event: SCXML.Event<AnyEventObject> | SingleOrArray<any>, payload?: EventData | undefined) => State<any>
