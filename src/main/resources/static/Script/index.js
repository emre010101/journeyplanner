// This is a placeholder for the logged-in user.
// In a real application, you would get this data from your server.
var loggedInUser = null;

window.onload = function() {
    var signInButton = document.getElementById('sign-in');
    var logInButton = document.getElementById('log-in');
    var userGreeting = document.getElementById('user-greeting');

    if (loggedInUser) {
        signInButton.style.display = 'none';
        logInButton.style.display = 'none';
        userGreeting.textContent = 'Welcome, ' + loggedInUser;
    } else {
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
    var username = document.getElementById('loginUsername').value;
    var password = document.getElementById('loginPassword').value;

    // Call the loginUser function
    loginUser(username, password);
});

function loginUser(username, password) {
    // Create request payload
    var payload = {
        username: username,
        password: password
    };

    // Send POST request to /login
    return fetch('http://localhost:8082/api/login', {
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
        return response.json();
    })
    .then(data => {
        // Handle response here. If login is successful,
        // set the loggedInUser variable and update the UI.
        loggedInUser = username;
        loginModal.style.display = "none";
        window.onload();
    })
    .catch((error) => {
        console.error('Error:', error);
    });
}
