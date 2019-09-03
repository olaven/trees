import * as React from "react";
import { render } from "react-dom"
import {Map} from "./map";
import {TestModule} from "./testModule";


const App = () => {

    const locations = [];

    return <div>
        <TestModule/>
        <Map/>
    </div>
};


render(<App/>, document.getElementById("root"));