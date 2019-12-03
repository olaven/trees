import {useEffect, useState} from "react";
import {apiFetch} from "../fetch";

export const useGetLocations = () => {

    const [ locations, setLocations ] = useState([]);

    useEffect(() => {

        const loadLocations = async () => {

            const response = await apiFetch("/api/locations");
            if (response.status !== 200) {

                throw "failed to fetch from server";
            }

            const page = await response.json();
            const {locations, next} = page.data;
            setLocations(locations);
        };

        loadLocations();
    }, []);

    return locations;
};