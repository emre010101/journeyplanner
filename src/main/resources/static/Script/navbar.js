// This is a placeholder for the logged-in user.
//If the server confirm user authentication, the user will be saved here
var loggedInUser = null; //'emre.kavak3938@gmail.com'

//Adding window event listener to load external html components and other functionalities
window.addEventListener("load", function() {
    console.log("Testing");
    Promise.all([ //Waiting to load the other components
        loadComponent('nav-bar', 'pages/navbar.html'),
        loadComponent('logInModal', 'pages/loginModal.html'),
        loadComponent('signInModal', 'pages/signinModal.html')
    ])
    .then(() => {

        var elements = assignButtonAndModals();
        assignEventListeners(elements);

        // Set the page state depending on the logged in user
        setPageState(loggedInUser, elements);

        // Handle the login form submission
        handleLoginFormSubmission(elements);

        //Handle the sign-in form submission
        handleSignInFormSubmission(elements);
    })
    .catch((error) => {
        console.error("Error: ", error);
    });
});

function loadComponent(elementId, componentPath) {
    return new Promise((resolve, reject) => {
        const xhttp = new XMLHttpRequest();

        xhttp.onload = function() {
            const container = document.getElementById(elementId);
            container.innerHTML = this.responseText;
            // Using setTimeout to wait until the new HTML is inserted into the DOM
            setTimeout(() => resolve(), 0);
        }

        xhttp.onerror = reject;

        xhttp.open("GET", componentPath, true);
        xhttp.send();
    });
}


function assignButtonAndModals() {
    //Declare your variables here
    var logInModal, signInModal, logInButton, signInButton, closeButtons;

    //Get the modals
    logInModal = document.getElementById("logInModal");
    signInModal = document.getElementById("signInModal");

    //Get the buttons that opens the modals
    logInButton = document.getElementById("log-in");
    signInButton = document.getElementById("sign-in");

    //Get the <span> elements that closes the modals
    closeButtons = document.getElementsByClassName("close");

    //Return the variables as an object
    return {logInModal, signInModal, logInButton, signInButton, closeButtons};
}

//Event Listeners will be added after they are returned
function assignEventListeners(elements){

    //When the user clicks it, open the modal
    elements.logInButton.onclick = function(){
        console.log("Login is clicked");
        elements.logInModal.style.display = "block";
    }
    elements.signInButton.onclick = function() {
        elements.signInModal.style.display = "block";
    }

    // When the user clicks on <span> (x), close the modal
    for (let i = 0; i < elements.closeButtons.length; i++) {
        elements.closeButtons[i].onclick = function() {
            elements.closeButtons[i].parentElement.parentElement.style.display = "none";
        }
    }

    // When the user clicks anywhere outside of the modal, close it
    window.onclick = function(event) {
      if (event.target == logInModal) {
        elements.logInModal.style.display = "none";
      } else if (event.target == signInModal) {
        elements.signInModal.style.display = "none";
      }
    }
}



//////////////////////////////////////////////////////////////////

// Handle the login form submission
function handleLoginFormSubmission(elements) {
    document.getElementById('loginForm').addEventListener('submit', function(event) {
        // Prevent the form from being submitted normally and avoid reload the page
        event.preventDefault();

        // Get username and password from the form
        var loginEmail = document.getElementById('loginEmail').value;
        var password = document.getElementById('loginPassword').value;

        // Call the loginUser function
        loginUser(loginEmail, password, elements);
    });
}


function loginUser(loginEmail, password, elements) {
    // Create request payload
    var payload = {
        email: loginEmail,
        password: password
    };

    // Send POST request to /login
    return fetch('http://localhost:8082/api/jp/auth/authenticate', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify(payload)
    })
    .then(response => {
        if (!response.ok) {
            throw new Error('Network response was not ok');
        }
        console.log(response.status);
        return response.text();
    })
    .then(text => {
        console.log(text);
        const data = JSON.parse(text);

        // Handle response here. If login is successful,
        // set the loggedInUser variable and update the UI.
        loggedInUser = loginEmail;
        elements.logInModal.style.display = "none";
        setPageState(loggedInUser, elements);

        //Store JWT token in localStorege
        localStorage.setItem('accessToken', data.access_token);
        localStorage.setItem('loggedInUser', loggedInUser);

    })
    .catch((error) => {
        console.error('Error:', error);
    });
}
////////////////////////////////////////////////////////////////////////////////////////////////////////

// Handle the sign in form submission
function handleSignInFormSubmission(elements){
    document.getElementById('signinForm').addEventListener('submit', function(event) {
        // Prevent the form from being submitted normally and avoid reload the page
        event.preventDefault();

        // Get username and password from the form
        var signinEmail = document.getElementById('signinEmail').value;
        var password = document.getElementById('signinPassword').value;
        var signinFirstName = document.getElementById('signinFirstName').value;
        var signinLastName = document.getElementById('signinLastName').value;
        console.log(signinEmail, password, signinFirstName, signinLastName);
        // Call the loginUser function
        signinUser(signinEmail, password, signinFirstName, signinLastName, elements);
    });
}


function signinUser(signinEmail, password, signinFirstName, signinLastName, elements) {
    // Create request payload
    var payload = {
        email: signinEmail,
        password: password,
        firstname: signinFirstName,
        lastname: signinLastName
    };

    // Send POST request to /login
    return fetch('http://localhost:8082/api/jp/auth/register', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify(payload)
    })
    .then(response => {
        if (!response.ok) {
            throw new Error('Network response was not ok');
        }
        console.log(response.status);
        return response.text();
    })
    .then(text => {
        console.log(text);
        const data = JSON.parse(text);

        // Handle response here. If login is successful,
        // set the loggedInUser variable and update the UI.
        loggedInUser = signinEmail;
        elements.signInModal.style.display = "none";
        setPageState(loggedInUser, elements);

        //Store JWT token in localStorege
        localStorage.setItem('accessToken', data.access_token);
        localStorage.setItem('loggedInUser', loggedInUser);
    })
    .catch((error) => {
        console.error('Error:', error);
    });
}

function setPageState(loggedInUser, elements) {
    var logOutButton = document.getElementById('log-out');
    var userGreeting = document.getElementById('user-greeting');

    if (!elements.signInButton || !elements.logInButton || !logOutButton || !userGreeting) {
        console.error('One or more elements are missing.');
        return;
    }

    if (loggedInUser) {
        elements.signInButton.style.display = 'none';
        elements.logInButton.style.display = 'none';
        logOutButton.style.display = 'inline-block';
        userGreeting.textContent = 'Welcome, ' + loggedInUser;
    } else {
        logOutButton.style.display = 'none';
        elements.signInButton.style.display = 'inline-block';
        elements.logInButton.style.display = 'inline-block';
        userGreeting.textContent = '';
    }
}


////////////////////////////////////////////////////////////////////



/* //implement this function for log out and also carry all logic for the login signin and logut to different js
fetch('http://localhost:8082/api/jp/auth/logout', {
  method: 'POST',
  headers: {
    'Authorization': 'Bearer ' + localStorage.getItem('accessToken'),
    'Content-Type': 'application/json'
  },
})
.then(response => {
  if (!response.ok) {
    throw new Error('Network response was not ok');
  }
  // Remove the access token from local storage
  localStorage.removeItem('accessToken');
})
.catch((error) => {
  console.error('Error:', error);
});

*/

