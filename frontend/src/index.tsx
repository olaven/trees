import * as React from "react";
import {render} from "react-dom"
import {MapComponent} from "./map/mapComponent";


const App = () => {
    return <div>
        <MapComponent/>
    </div>
};


render(<App/>, document.getElementById("root"));