let map, directionsService, directionsRenderer;

// function to handle button click event
function handleButtonClick() {
    console.log("Button was clicked");
    var userInput = getUserInput();
    processUserInput(userInput);
}

// function to get user input
function getUserInput() {
    var userInput = document.getElementById('user-input').value;
    console.log(userInput);
    return userInput;
}

// function to process user input
function processUserInput(userInput) {
    fetch('http://localhost:8082/api/journey', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify({
            message: userInput
        })
    })
    //Arguments acts like variables within the function
    .then(response => response.text()) // Get the response body as text
    .then(data => processText(data)) // Call the processText function with the data "resulting text is named 'data'"
    .catch((error) => {
        console.error('Error:', error);
    });
}

// function to process the text from the response
function processText(text) {
    let data = JSON.parse(text);
    console.log("Let see if it will print here")
    console.log(JSON.stringify(data, null, 2));
    console.log("Origin: " + data.origin);
    console.log("Destination: " + data.destination);

    let request = {
        origin: data.origin,
        destination: data.destination,
        travelMode: 'DRIVING'
    };

    routeDirection(request);
}

// function to route direction
function routeDirection(request) {
    directionsService.route(request, function(result, status) {
    //console.log(result);
        if (status == 'OK') {
            // Set the directions on the map
            directionsRenderer.setDirections(result);
        } else {
            console.error('Directions request was not successful for the following reason: ' + status);
        }
    });
}

// Add event listener to the button
document.getElementById('send-button').addEventListener('click', handleButtonClick);
// Get your input field
var inputField = document.getElementById('user-input');

// Listen for keydown event
inputField.addEventListener('keydown', function(event) {
    // Check if the key is the "Enter" key
    if (event.key === "Enter") {
        // Prevent the default action to stop the form from submitting
        event.preventDefault();
        // Call your function
        handleButtonClick();
    }
});

