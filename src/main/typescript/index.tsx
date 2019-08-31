import * as React from "react";
import { render } from "react-dom"
import {MyMap} from "./map";


const App = () => {

    return <div>
        <MyMap/>
    </div>
};

render(<App/>, document.getElementById("root"));