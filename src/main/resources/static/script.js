console.log("Script is running");

let map;

async function initMap() {
  const { Map } = await google.maps.importLibrary("maps");
  map = new Map(document.getElementById("map"), {
    center: { lat: -34.397, lng: 150.644 },
    zoom: 8,
  });
}

initMap();

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
  .then(response => response.text())
  .then(data => {
      console.log(data);
  })
  .catch((error) => {
      console.error('Error:', error);
  });
});
