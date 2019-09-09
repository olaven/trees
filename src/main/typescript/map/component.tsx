import * as React from "react";
import {useEffect, useState} from "react";
import {initializeMap, loadMap} from "./utils";
import {useGetLocations} from "./effects";


export const  Map = () => {

    const [ map, setMap ] = useState(null);
    const locations = useGetLocations() as [];

    console.log("locations", locations); 
    useEffect(() => {

        const map = initializeMap();
        setMap(map)
    }, []);

    useEffect(() => {

        if(!map) return;
        loadMap(map, locations);
    }, [locations]);

    return <div id={"map"} style={{height: "100vh", width: "100vw"}}/>
};

