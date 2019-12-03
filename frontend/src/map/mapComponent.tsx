import * as React from "react";
import {useEffect, useState} from "react";
import {initializeMap, loadMap} from "./mapUtils";
import {getLocations} from "./mapEffects";
import { Map } from "mapbox-gl"


export const MapComponent = () => {

    const [ map, setMap ] = useState<Map>(null);
    const locationPage = getLocations(); //TODO: load more using .next. On map move, fetch some longer out in proximity

    useEffect(() => {

        const map = initializeMap();
        setMap(map)
    }, []);

    useEffect(() => {

        if(!map) return;
        loadMap(map, locationPage.locations);
    }, [locationPage]);

    return <div id={"map"} style={{height: "100vh", width: "100vw"}}/>
};

