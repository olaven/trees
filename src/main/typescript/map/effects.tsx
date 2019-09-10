import {useEffect, useState} from "react";

export const useGetLocations = () => {

    const [ locations, setLocations ] = useState([]);

    useEffect(() => {

        const loadLocations = async () => {

            const response = await fetch("trees/api/locations");
            const locations = await response.json();
            setLocations(locations);
        };

        loadLocations();
    }, []);

    return locations;
};