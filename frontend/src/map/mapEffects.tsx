import {useEffect, useState} from "react";
import {apiFetch} from "../fetch";

interface LocationPage {
    locations: [], //TODO: type
    next: string
}

export const getLocations = () => {

    const [ locations, setLocations ] = useState<LocationPage>({locations: [], next: null});

    useEffect(() => {

        const loadLocations = async () => {

            const response = await apiFetch("/api/locations");
            if (response.status !== 200) {

                throw "failed to fetch from server";
            }

            const page = await response.json();
            setLocations({
                locations: page.data.list,
                next: page.data.next
            });
        };

        loadLocations();
    }, []);

    return locations;
};