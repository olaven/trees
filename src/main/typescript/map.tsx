import * as React from "react";
import * as Mapbox from "mapbox-gl";
import {useEffect} from "react";

export const MyMap = () => {

    useEffect(() => {

        console.log(process.env.MAPBOX_KEY);
        (Mapbox.accessToken as any) = process.env.MAPBOX_KEY;
        new Mapbox.Map({
            container: 'map', // container id
            style: 'mapbox://styles/mapbox/streets-v11', // stylesheet location
            center: [-74.50, 40], // starting position [lng, lat]
            zoom: 9// starting zoom
        });
    }, []);

    return <div id={"map"} style={{height: "100vh", width: "100vw"}}/>
};
