import * as React from "react";
import { render } from "react-dom"
import {Map} from "./map";


const App = () => {

    const locations = [];

    return <div>
        <Map/>
    </div>
};


render(<App/>, document.getElementById("root"));