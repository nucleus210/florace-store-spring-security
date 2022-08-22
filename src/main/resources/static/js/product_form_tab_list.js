import { get, post } from "../src/api.js";


const lists = document.querySelectorAll(".nav-item a");
const contentList = document.querySelectorAll(".tab-content div");
// document.getElementById('project-resource-submit').addEventListener('click', onResourceSubmit);
let uploadElement = document.getElementById('upload_list');
document.getElementById('uploadFileSubmitBtn').addEventListener('click', onFileSubmit);
lists.forEach(element => {
    element.addEventListener('click', doSomething);
})
let uploadList = document.getElementById('upload_list');
let storageIds = document.getElementById('storage-entities');
let uploads = [];

function doSomething() {
    [].map.call(lists, function (elem) {
        elem.classList.remove("active")
    });
    this.classList.add("active");
    let link = this.getAttribute("href").substring(1);

    [].map.call(contentList, function (elem) {
        elem.classList.remove("show");
        elem.classList.remove("active");
    });
    myFunction(link);
};

[].map.call(lists, function (elem) {
    elem.addEventListener("click", doSomething);
});

function myFunction(link) {
    const list = document.getElementById(link).classList;
    list.add("show");
    list.add("active");
}

function loadHTML(link) {
    // document.getElementById(link).innerHTML='<object type="text/html" data="/templates/login.html" ></object>';
    fetch(link)
        .then(response => response.text())
        .then(text => {
            const toNodes = new DOMParser().parseFromString(text, 'text/html').body.childNodes[0];
        });
}


/**
 * Working method - POST file multipart form data REST approach to server.
 *
 * First add custom metadata tag inside <head></head> of Thymeleaf template to access to the CSRF token
 *
 *     <head>
 *      <meta name="_csrf" th:content="${_csrf.token}"/>
 *      <meta name="_csrf_header" th:content="${_csrf.headerName}"/>
 *     </head>
 *
 * In JS access CSRF token from metadata tag
 *
 *     let token = $("meta[name='_csrf']").attr("content");
 *     let header = $("meta[name='_csrf_header']").attr("content");
 *
 * Consume file input form data in JS
 *
 *     const formData = new FormData();
 *     const fileField = document.querySelector('input[type="file"]');
 *     formData.append('file', fileField.files[0]);
 *
 * Finally create custom Headers and add CSRF token to header!!!!!! Or we get 403
 * Don't set Content-type!!!
 *
 *     const customHeader = {
 *         method: 'POST',
 *         charset: 'UTF-8',
 *         headers: {},
 *         body: formData
 *     }
 *
 *     customHeader.headers['X-CSRF-TOKEN'] = token;
 *
 * then use fetch API
 * Awesome!!! Cheers!!!
 *
 * **/

async function onFileSubmit(event) {
    event.preventDefault();
    let token = $("meta[name='_csrf']").attr("content");
    let header = $("meta[name='_csrf_header']").attr("content");

    const formData = new FormData();
    const fileField = document.querySelector('input[type="file"]');
    formData.append('file', fileField.files[0]);

    const customHeader = {
        method: 'POST',
        charset: 'UTF-8',
        headers: {},
        body: formData
    }

    customHeader.headers['X-CSRF-TOKEN'] = token;

    let result = await request('http://localhost:8080/storage/upload/', customHeader);
    console.log(result);
    let responseMessage = {
        responseMessage: result.responseMessage

    }
    console.log(responseMessage)

    let entity = result.storageEntity;

    let storageEntity = {
        resourceId: entity.resourceId,
        fileName: entity.fileName,
        fileUrl: entity.fileUrl,
        size: entity.size
    }

        console.log(storageEntity);

    let paragraphElement = document.createElement('p');
    paragraphElement.style.color = 'black'
    paragraphElement.innerText = responseMessage.responseMessage;
    uploadElement.appendChild(paragraphElement);
    uploads.push(entity.resourceId)
    storageIds.value = uploads;

    console.log(storageIds.value);

    // let respond = await fetch('http://localhost:8080/storage/upload/', customHeader)
    //     .then((response) => response.json())
    //     .then((result) => {
    //         console.log('Success:', result);
    //         return result;
    //     })
    //     .catch((error) => {
    //         console.error('Error:', error);
    //         handleServerResponseError(error);
    //         return error;
    //     });
}

async function request(uri, options) {

    try {
        const serverResponse = await fetch( uri, options);

        if (serverResponse.ok === false) {
            if (serverResponse.status === 403) {
                return new Error('Unauthorized request!');
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
        console.log(err.message);
        return err.message;
    }
}
async function requestf(url, headers) {

    fetch(url, headers)
        .then((response) => response.json())
        .then((result) => {
            console.log('Success:', result);
            return result;
        })
        .catch((error) => {
            console.error('Error:', error);
            handleServerResponseError(error);
            return error;
        });
}

async function handleServerResponseData(response) {

    let objects = response;

    console.log(objects);

}

async function handleServerResponseError(response) {


}
//
// postData('https://example.com/answer', { answer: 42 })
//     .then((data) => {
//         console.log(data); // JSON data parsed by `data.json()` call
//     });
