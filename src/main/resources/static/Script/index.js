// This is a placeholder for the logged-in user.
// In a real application, you would get this data from your server.

window.onload = function() {
    console.log("TEsting");
    loadComponent('nav-bar', 'pages/navbar.html');
    loadComponent('loginModal', 'pages/loginModal.html');
    loadComponent('signinModal', 'pages/signinModal.html');
}

function loadComponent(elementId, componentPath) {
    const xhttp = new XMLHttpRequest();

    xhttp.onload = function() {
        document.getElementById(elementId).innerHTML = this.responseText;
    }
    xhttp.open("GET", componentPath, true);
    xhttp.send();
}

