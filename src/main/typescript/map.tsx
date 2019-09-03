import * as React from "react";
import * as Mapbox from "mapbox-gl";
import {useEffect, useState} from "react";

const useGetLocations = () => {

    const [ locations, setLocations ] = useState([]);

    useEffect(() => {

        const loadLocations = async () => {

            const response = await fetch("/trees/api/locations");
            const locations = await response.json();
            setLocations(locations);
        };

        loadLocations();
    }, []);

    return locations;
};

export const Map = () => {


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

    const locations = useGetLocations();
    console.log("locations from hook: ", locations);

    return <div id={"map"} style={{height: "100vh", width: "100vw"}}/>
};

