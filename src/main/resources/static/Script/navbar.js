// This is a placeholder for the logged-in user.
// In a real application, you would get this data from your server.
/*var loggedInUser = null; //'emre.kavak3938@gmail.com'

window.onload = function() {
    var signInButton = document.getElementById('sign-in');
    var logInButton = document.getElementById('log-in');
    var logOutButton = document.getElementById('log-out')
    var userGreeting = document.getElementById('user-greeting');

    if (loggedInUser) {
        signInButton.style.display = 'none';
        logInButton.style.display = 'none';
        logOutButton.style.display = 'inline-block';
        userGreeting.textContent = 'Welcome, ' + loggedInUser;
    } else {
        logOutButton.style.display = 'none';
        signInButton.style.display = 'inline-block';
        logInButton.style.display = 'inline-block';
        userGreeting.textContent = '';
    }
}

// Get the modals
var loginModal = document.getElementById("loginModal");
var signinModal = document.getElementById("signinModal");

// Get the buttons that opens the modals
var loginButton = document.getElementById("log-in");
var signinButton = document.getElementById("sign-in");

// Get the <span> elements that closes the modals
var closeButtons = document.getElementsByClassName("close");

// When the user clicks on the button, open the modal
loginButton.onclick = function() {
  loginModal.style.display = "block";
}

signinButton.onclick = function() {
  signinModal.style.display = "block";
}

// When the user clicks on <span> (x), close the modal
for (let i = 0; i < closeButtons.length; i++) {
  closeButtons[i].onclick = function() {
    closeButtons[i].parentElement.parentElement.style.display = "none";
  }
}

// When the user clicks anywhere outside of the modal, close it
window.onclick = function(event) {
  if (event.target == loginModal) {
    loginModal.style.display = "none";
  } else if (event.target == signinModal) {
    signinModal.style.display = "none";
  }
}

//////////////////////////////////////////////////////////////////

// Handle the login form submission
document.getElementById('loginForm').addEventListener('submit', function(event) {
    // Prevent the form from being submitted normally and avoid reload the page
    event.preventDefault();

    // Get username and password from the form
    var loginEmail = document.getElementById('loginEmail').value;
    var password = document.getElementById('loginPassword').value;

    // Call the loginUser function
    loginUser(loginEmail, password);
});

function loginUser(loginEmail, password) {
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
        loginModal.style.display = "none";
        window.onload();

        //Store JWT token in localStorege
        localStorage.setItem('accessToken', data.access_token);
    })
    .catch((error) => {
        console.error('Error:', error);
    });
}
////////////////////////////////////////////////////////////////////////////////////////////////////////

// Handle the sign in form submission
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
    signinUser(signinEmail, password, signinFirstName, signinLastName);
});

function signinUser(signinEmail, password, signinFirstName, signinLastName) {
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
        signinModal.style.display = "none";
        window.onload();

        //Store JWT token in localStorege
        localStorage.setItem('accessToken', data.access_token);
    })
    .catch((error) => {
        console.error('Error:', error);
    });
}


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

