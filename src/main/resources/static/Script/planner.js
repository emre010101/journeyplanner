let map, directionsService, directionsRenderer, geocoder;
let globalCounter = 0;

async function handleButtonClick() {
    const userInput = document.getElementById('user-input').value;
    const data = await fetchData(userInput);
    processText(data);
}

async function fetchData(userInput) {
    try {
        const response = await fetch('http://localhost:8082/api/jp/gpt/analyze', {
            method: 'POST',
            headers: {
                'Authorization': 'Bearer ' + localStorage.getItem('accessToken'),
                'Content-Type': 'application/json'
            },
            body: JSON.stringify({
                message: userInput
            })
        });

        return await response.json();
    } catch (error) {
        console.error('Error:', error);
    }
}

async function processText(data) {
    const request = {
        origin: data.origin,
        destination: data.destination,
        travelMode: 'DRIVING'
    };

    routeDirection(request);

    console.log("To see the format of the data" + data.stops);

    if(Object.keys(data.stops).length > 0){
        await displayStops(data.stops);
        await plotStopsOnMap();
    }

    const geocodedOrigin = await getGeocodedAddress(data.origin);
    const geocodedDestination = await getGeocodedAddress(data.destination);

    addPointToBar('origin', data.origin, true, geocodedOrigin);
    addPointToBar('destination', data.destination, true, geocodedDestination);
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

async function displayStops(stops) {
    let stopsList = document.getElementById('stops-list');
    stopsList.innerHTML = '';

    for (let title in stops) {
        let stopsListContent = `<h2>${title}</h2><ul>`;

        for (const stop of stops[title]) {
            globalCounter++;
            let geocodedAddress = await getGeocodedAddress(stop);
            let stopId = stop.replace(/ /g,'_') + '_' + Date.now();
            stopsListContent += `<li draggable="true" ondragstart="drag(event)" id="${stopId}" data-stop-number="${globalCounter}" data-geocoded-address="${geocodedAddress}">${globalCounter}. ${stop}</li>`;
        }

        stopsListContent += '</ul>';
        stopsList.innerHTML += stopsListContent;
    }

    stopsList.style.display = stopsList.innerHTML ? 'block' : 'none';
}

async function plotStopsOnMap() {
    let stopsListItems = document.getElementById('stops-list').getElementsByTagName('li');

    for (let item of stopsListItems) {
        let geocodedAddress = item.getAttribute('data-geocoded-address');

        if (geocodedAddress) {
            let geocodedLocation = geocodedAddress.split(',');
            new google.maps.Marker({
                map: map,
                position: { lat: parseFloat(geocodedLocation[0]), lng: parseFloat(geocodedLocation[1]) },
                label: item.getAttribute('data-stop-number')
            });
        } else if (geocodedAddress === "") {
            console.error('Geocode data not found for stop: ' + item.id);
        }
    }
}


function getGeocodedAddress(stop) {
    return new Promise((resolve) => {
    console.log("Looking for geocoded adreess for stop: " + stop);
        geocoder.geocode({ 'address': stop }, function(results, status) {
            if (status === google.maps.GeocoderStatus.OK) {
                let location = results[0].geometry.location;
                resolve(location.lat() + ',' + location.lng());
            } else {
                console.error('Geocode error: ' + status);
                resolve('');
            }
        });
    });
}



///////////////////////////////////////////////////////////////////////////////////
function addPointToBar(id, text, isOriginDestination = false, geocodedAddress) {
    let spanElement = document.createElement('span');
    spanElement.setAttribute('class', 'journey-point');
    spanElement.setAttribute('id', id);
    spanElement.setAttribute('draggable', 'true');
    spanElement.setAttribute('data-geocoded-address', geocodedAddress);  // Set the geocoded address
    spanElement.setAttribute("ondragstart", "drag(event)");  // fire the "drag" event
    spanElement.textContent = text;
    if (isOriginDestination) {
        spanElement.classList.add('origin-destination-point');
    } else {
        spanElement.onclick = function (event) { removePoint(event); };
    }

    let bar = document.getElementById('journey-bar');

    if (!Array.from(bar.childNodes).some(el => el.textContent === text)) {
        let destinationPoint = document.querySelector('.origin-destination-point:last-child');
        bar.insertBefore(spanElement, destinationPoint);
    }
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
    console.log('Drop event data:', data);  // Add logging here
    var bar = document.getElementById("journey-bar");
    var target = ev.target;
    var draggedElement = document.getElementById(data);  // Get the dragged element
    console.log('Dragged element:', draggedElement);  // And here

    // check if the bar already contains this point
    if (bar.contains(draggedElement)) {
        // if target is another journey point (but not an origin-destination-point), swap their positions
        if (target.classList.contains('journey-point') && !target.classList.contains('origin-destination-point')) {
            swapNodes(draggedElement, target);
        }
        return;
    }

    // if target is the journey bar or another journey point (but not an origin-destination-point), add a new journey point
    else if (target.id === "journey-bar" ||
        (target.classList.contains('journey-point') && !target.classList.contains('origin-destination-point'))) {
        let geocodedAddress = draggedElement.getAttribute('data-geocoded-address');  // Get the geocoded address

        // Get the text of the dragged element, and not the id
        let text = draggedElement.textContent;

        // Pass the original ID of the dragged element (which includes the timestamp), the text, and the geocoded address to addPointToBar
        addPointToBar(data, text, false, geocodedAddress);
    }
}

function swapNodes(node1, node2) {
    console.log("SWAPNODES IS INVOKED swapping " + node1 + "\n" + " and " + "\n" + node2)

    // Do not swap if either node is an origin or destination point
    if (node1.classList.contains('origin-destination-point') || node2.classList.contains('origin-destination-point')) {
        return;
    }

    // Create marker for the original spot of node2
    let marker = document.createElement('span');
    node2.parentNode.insertBefore(marker, node2);

    // Move node1 to node2's original spot
    node2.parentNode.insertBefore(node1, node2);

    // Wait for the DOM to be updated, then move node2 to node1's original spot
    setTimeout(() => {
        marker.parentNode.insertBefore(node2, marker);

        // Get rid of the marker
        marker.parentNode.removeChild(marker);
    }, 0);  // 0 milliseconds delay, meaning run this code as soon as possible but after any pending changes have been made to the DOM
}



    document.getElementById('journey-bar').addEventListener('drop', drop);
    document.getElementById('journey-bar').addEventListener('dragover', allowDrop);

    document.getElementById('send-button').addEventListener('click', handleButtonClick);













    //Buttons to display and save the route
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



    //initialising the field
    var inputField = document.getElementById('user-input');

    //Adding event listener for enter button to allow user friendly platform
    inputField.addEventListener('keydown', function(event) {
        if (event.key === "Enter") {
            event.preventDefault();
            handleButtonClick();
        }
    });

