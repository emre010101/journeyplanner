let map, directionsService, directionsRenderer;


document.getElementById('send-button').addEventListener('click', function() {
  console.log("Button was clicked");
  var userInput = document.getElementById('user-input').value;
  console.log(userInput);

  fetch('http://localhost:8082/api/journey', {
    method: 'POST',
    headers: {
        'Content-Type': 'application/json'
    },
    body: JSON.stringify({
        message: userInput
    })
  })
 .then(response => response.text()) // Get the response body as text
 .then(text => {
     let data = JSON.parse(text); // Parse the text into a JavaScript object

     console.log("Origin: " + data.origin);
     console.log("Destination: " + data.destination);

     let request = {
       origin: data.origin,
       destination: data.destination,
       travelMode: 'DRIVING'
     };


    directionsService.route(request, function(result, status) {
      if (status == 'OK') {
        // Set the directions on the map
        directionsRenderer.setDirections(result);
      } else {
        console.error('Directions request was not successful for the following reason: ' + status);
      }
    });
  })
  .catch((error) => {
      console.error('Error:', error);
  });
});
