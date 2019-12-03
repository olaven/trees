export const apiFetch = (path: string, init: RequestInit = {}) => {

    const port = (process.env.NODE_ENV === "production"? process.env.API_PORT_PRODUCTION: process.env.API_PORT_DEVELOPMENT);
    const url = `http://localhost:${port}${path}`;
    return fetch(url, init)
};

