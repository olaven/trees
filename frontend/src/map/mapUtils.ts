import * as Mapbox from "mapbox-gl"


const toMarkers = (locations: []) => {

    /**
     * Converts given location to marker.
     */
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

    return {
        "type": "FeatureCollection",
        "features": locations.map(location => toMarker(location))
    };
};



export const initializeMap = () => {

    (Mapbox.accessToken as any) = process.env.MAPBOX_KEY;

    return new Mapbox.Map({
        container: 'map', // container id
        style: 'mapbox://styles/mapbox/streets-v11', // stylesheet location
        center: [28.2278, 36.4511], // starting position [lng, lat]
        zoom: 2// starting zoom
    });
};


export const loadMap = (map: Mapbox.Map, locations: []) => {

    const markers = toMarkers(locations);
    map.on("load", () => {

        map.loadImage("https://i.imgur.com/MK4NUzI.png", (error, image) => {

            if (error) throw error;
            map.addImage("custom-marker", image);
            // @ts-ignore
            map.addLayer({
                id: "markers",
                type: "symbol",
                source: {
                    type: "geojson",
                    data: markers
                },
                layout: {
                    "icon-image": "custom-marker",
                }
            });
        });
    });
};

