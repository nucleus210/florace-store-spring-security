//declare html elements
const img = document.getElementById('photo');
const file = document.getElementById('inputProfileImage');
const submitBtn = document.getElementById('profilePhotoSubmitBtn')

//function to prevent redirection on onSubmit action and call rest services to upload profile photo
submitBtn.addEventListener('click', onSubmit)


//function for showing photo when we choose an image to upload
file.addEventListener('change', function () {
    const chosenFile = this.files[0];
    if (chosenFile) {
        const reader = new FileReader();
        reader.addEventListener('load', function () {
            img.setAttribute('src', reader.result);
        });
        reader.readAsDataURL(chosenFile);
    }
});

async function onSubmit(event) {
    event.preventDefault();
    showToastMessage("File uploaded successfully.");
    getFileName(file);
}

function showToastMessage(message) {
    // Get the snackbar DIV
    const x = document.getElementById("snackbar");
    const paragraphElement = document.createElement("p");
    paragraphElement.textContent = message;
    x.appendChild(paragraphElement);
    // Add the "show" class to DIV
    x.className = "show";

    // After 3 seconds, remove the show class from DIV
    setTimeout(function () {
        x.className = x.className.replace("show", "");
    }, 3000);
    setTimeout(function () {
        x.removeChild(paragraphElement);
    }, 4000);
}

function getFileName(filename) {

    const fullPath = filename.value;
    if (fullPath) {
        const startIndex = (fullPath.indexOf('\\') >= 0 ? fullPath.lastIndexOf('\\') : fullPath.lastIndexOf('/'));
        let filename = fullPath.substring(startIndex);
        if (filename.indexOf('\\') === 0 || filename.indexOf('/') === 0) {
            filename = filename.substring(1);
        }
        alert(filename);
    }
}


