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

const toMarker = location => {

    return {
        "type": "Feature",
        "geometry": {
            "type": "Point",
            "coordinates": [
                location.y,
                location.x
            ]
        },
        "properties" : {
            'marker-color': '#00d021',
            'marker-size': '42',
            'marker-symbol': 'stadium'
        }
    };
};

const toMarkers = (locations: []) => {

    return {
        "type": "FeatureCollection",
        "features": locations.map(location => toMarker(location))
    };
};


export const Map = () => {

    const [ map, setMap ] = useState(null);

    useEffect(() => {

        console.log(process.env.MAPBOX_KEY);
        (Mapbox.accessToken as any) = process.env.MAPBOX_KEY;
        const map = new Mapbox.Map({
            container: 'map', // container id
            style: 'mapbox://styles/mapbox/streets-v11', // stylesheet location
            center: [28.2278, 36.4511], // starting position [lng, lat]
            zoom: 9// starting zoom
        });
        setMap(map)
    }, []);


    const locations = useGetLocations() as [];

    useEffect(() => {

        const markers = toMarkers(locations);
        if (map) {

            console.log("added layer with markers: ", markers);
            map.on('load', event => {
                // Add the data to your map as a layer
                map.addLayer({
                    id: 'locations',
                    type: 'symbol',
                    // Add a GeoJSON source containing place coordinates and information.
                    source: {
                        type: 'geojson',
                        data: markers
                    }
                });

                console.log("added layer with markers: ", markers)
            });
        }
    }, [locations]);

    return <div id={"map"} style={{height: "100vh", width: "100vw"}}/>
};

