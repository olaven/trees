import {useEffect, useState} from "react";

export const useGetLocations = () => {

    const [ locations, setLocations ] = useState([]);

    useEffect(() => {

        const loadLocations = async () => {

            const response = await fetch("api/locations");
            if (response.status !== 200) {

                throw "failed to fetch from server";
            }

            const wrappers = await response.json();
            const locations = wrappers.map(wrapper => wrapper.data);
            setLocations(locations);
        };

        loadLocations();
    }, []);

    return locations;
};