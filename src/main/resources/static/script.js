let map, directionsService, directionsRenderer;

function handleButtonClick() {
    var userInput = getUserInput();
    processUserInput(userInput);
}

function getUserInput() {
    var userInput = document.getElementById('user-input').value;
    return userInput;
}

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
    .then(response => response.text())
    .then(data => processText(data))
    .catch((error) => {
        console.error('Error:', error);
    });
}

function processText(text) {
    let data = JSON.parse(text);

    let request = {
        origin: data.origin,
        destination: data.destination,
        travelMode: 'DRIVING'
    };

    routeDirection(request);

    if(Object.keys(data.stops).length > 0){
        displayStops(data.stops);
    }

    // Add origin and destination to the journey bar
    addJourneyPoint(data.origin, data.origin);
    addJourneyPoint(data.destination, data.destination);
}

function displayStops(stops) {
    let stopsList = document.getElementById('stops-list');
    let stopsListContent = '';

    for (let title in stops) {
        stopsListContent += `<h2>${title}</h2>`;
        stopsListContent += '<ul>';
        stops[title].forEach((stop) => {
            stopsListContent += `<li draggable="true" ondragstart="drag(event)" id="${stop}">${stop}</li>`;
        });
        stopsListContent += '</ul>';
    }

    if (stopsListContent) {
        stopsList.style.display = 'block';
        stopsList.innerHTML = stopsListContent;
    } else {
        stopsList.style.display = 'none';
    }
}

function addJourneyPoint(id, text) {
    let journeyBar = document.getElementById('journey-bar');
    let journeyPoint = document.createElement('span');
    journeyPoint.className = "journey-point";
    journeyPoint.id = id;
    journeyPoint.innerText = text;
    journeyPoint.draggable = "true";
    journeyPoint.addEventListener('dragstart', drag);
    journeyPoint.addEventListener('click', removePoint);
    journeyBar.appendChild(journeyPoint);
}

function removePoint(ev) {
    let element = ev.target;
    element.parentNode.removeChild(element);
}

function drag(ev) {
    ev.dataTransfer.setData("text", ev.target.id);
}

function allowDrop(ev) {
    ev.preventDefault();
}

function drop(ev) {
    ev.preventDefault();
    var data = ev.dataTransfer.getData("text");
    var element = document.getElementById(data);
    addJourneyPoint(element.id, element.innerText);
}

function routeDirection(request) {
    directionsService.route(request, function(result, status) {
        if (status == 'OK') {
            directionsRenderer.setDirections(result);
        } else {
            console.error('Directions request failed: ' + status);
        }
    });
}

document.getElementById('send-button').addEventListener('click', handleButtonClick);

var inputField = document.getElementById('user-input');

inputField.addEventListener('keydown', function(event) {
    if (event.key === "Enter") {
        event.preventDefault();
        handleButtonClick();
    }
});
