
const host = 'http://localhost:8008';

async function request(uri, options) {

    try {
        const serverResponse = await fetch(host + uri, options);

        if (serverResponse.ok === false) {
            if (serverResponse.status === 403) {
            }
            const error = await serverResponse.json();
            throw new Error(error.message);
        }

        if (serverResponse.status === 204) {
            return serverResponse;
        } else {
            return serverResponse.json();
        }


    } catch (err) {
        alert(err.message);
        throw err;
    }
}

export async function get(url) {
    return request(url, 'get');
}
export async function post(url, data) {
    return request(url, data);
}
export async function put(url, data) {
    return request(url, data);
}
export async function del(url) {
    return request(url, 'delete');
}