document.getElementById('send-button').addEventListener('click', function() {
    var userInput = document.getElementById('user-input').value;

    // Send user input to the backend
    fetch('http://localhost:8080/api/journey', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify({
            message: userInput
        })
    })
    .then(response => response.json())
    .then(data => {
        // TODO: Handle the response data from the server

        // Display the user's message in the chat
        var chatDisplay = document.getElementById('chat-display');
        var userMessage = document.createElement('p');
        userMessage.textContent = userInput;
        chatDisplay.appendChild(userMessage);

        // Clear the input box
        document.getElementById('user-input').value = '';
    })
    .catch((error) => {
        console.error('Error:', error);
    });
});


let map;
async function initMap() {
  const { Map } = await google.maps.importLibrary("maps");
  map = new Map(document.getElementById("map"), {
    center: { lat: -34.397, lng: 150.644 },
    zoom: 8,
  });
}

initMap();