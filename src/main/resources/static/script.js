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
    addPointToBar('origin', data.origin);
    addPointToBar('destination', data.destination);
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

function addPointToBar(id, text, isOriginDestination = false) {
    let spanElement = document.createElement('span');
    spanElement.setAttribute('class', 'journey-point');
    spanElement.setAttribute('id', id);
    spanElement.setAttribute('draggable', 'true');
    spanElement.textContent = text;
    if (isOriginDestination) {
        spanElement.classList.add('origin-destination-point');
    } else {
        spanElement.onclick = function (event) { removePoint(event); };
    }

    document.getElementById('journey-bar').appendChild(spanElement);
}


function removePoint(ev) {
    let element = ev.target;
    if (!element.classList.contains('origin-destination-point')) {
        element.parentNode.removeChild(element);
    }
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
    var bar = document.getElementById("journey-bar");
    var target = ev.target;

    // check if the bar already contains this point
    if (bar.contains(document.getElementById(data))) {
        // if target is another journey point, swap their positions
        if (target.classList.contains('journey-point')) {
            swapNodes(document.getElementById(data), target);
        }
        return;
    }

    // if target is the journey bar, add a new journey point
    else if (target.id === "journey-bar") {
        addPointToBar(data + Date.now(), data);
    }

}

function swapNodes(node1, node2) {
    var parent1 = node1.parentNode;
    var next1 = node1.nextSibling;
    var parent2 = node2.parentNode;
    var next2 = node2.nextSibling;

    // If the nodes are siblings, swapping is easy
    if (next1 === node2) {
        parent1.insertBefore(node2, node1);
    } else if (next2 === node1) {
        parent2.insertBefore(node1, node2);
    } else {
        // If the nodes are not siblings, it's a bit more complicated
        parent1.insertBefore(node2, next1);
        parent2.insertBefore(node1, next2);
    }
}

document.getElementById('journey-bar').addEventListener('drop', drop);
document.getElementById('journey-bar').addEventListener('dragover', allowDrop);




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

// Get your new buttons
var saveButton = document.getElementById('save-button');
var calculateButton = document.getElementById('calculate-button');

// Add event listeners for your new buttons
saveButton.addEventListener('click', saveRoute);
calculateButton.addEventListener('click', calculateRoute);

// Write your new functions
function saveRoute() {
    // Here you can implement the logic for saving the current route
    // You can get all the elements in the journey bar and save them in the order they are displayed
    var journeyPoints = Array.from(document.getElementById('journey-bar').children);
    var route = journeyPoints.map(point => point.innerText);
    console.log('Saving the following route: ', route);
    // Save the route array somewhere, like a database or local storage
}

function calculateRoute() {
    // Here you can implement the logic for calculating the route with Google Maps API
    // You can get all the elements in the journey bar and use them in the order they are displayed
    var journeyPoints = Array.from(document.getElementById('journey-bar').children);
    var route = journeyPoints.map(point => point.innerText);
    console.log('Calculating the route with the following stops: ', route);
    // Calculate the route with Google Maps API
}

