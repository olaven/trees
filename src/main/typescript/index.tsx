import * as React from "react";
import {render} from "react-dom"
import {Map} from "./map/component";


const App = () => {

    return <div>
        <Map/>
    </div>
};


render(<App/>, document.getElementById("root"));