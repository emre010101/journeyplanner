//For rendering the map and calling google api for services
let map, directionsService, directionsRenderer, geocoder;
//To share and save
let journeyDetails, createdMapImage, urlToGoGMap;
// Define the journeyDescription as a global variable
let journeyDescription = '';
//For labelling the Stop Points
let globalCounter = 0;
// declare global variables for origin and destination
let globalGeocodedOrigin, globalGeocodedDestination;
let globalOriginName, globalDestinationName;

async function handleButtonClick() {
    console.log("Handling the user click");
    const userInput = document.getElementById('user-input').value;
    const data = await fetchData(userInput);
    document.getElementById('calculate-button').style.display = 'block';
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

    //If there are any stops display them on the list and also create markers on the map
    if(Object.keys(data.stops).length > 0){
        await displayStops(data.stops);
        await plotStopsOnMap();
    }

    //Getting the geocoded address of origin and destination
    globalGeocodedOrigin = await getGeocodedAddress(data.origin);
    globalGeocodedDestination = await getGeocodedAddress(data.destination);

    //Store the names
    globalOriginName = data.origin;
    globalDestinationName = data.destination;

    //Adding them to bar
    addPointToBar('destination', globalDestinationName, true, globalGeocodedDestination, 'Destination');
    addPointToBar('origin', globalOriginName, true, globalGeocodedOrigin, 'Origin');
}

function routeDirection(payload, { titles = [] } = {}, displayUrl = false) {
    // Clear out the existing route
    directionsRenderer.setDirections({routes: []});

    directionsService.route(payload, function(result, status) {
        if (status == 'OK') {
            directionsRenderer.setDirections(result);

            if(displayUrl){
                // At this point, just calling 'computeJourneyDetails' and pass the 'result' to it
                journeyDetails = computeJourneyDetails(result, titles); //save in global to reuse

                console.log(journeyDetails);

                displayJourneyDetails(journeyDetails);

                createUrlForGoogleMap(journeyDetails);
            }

        } else {
            console.error('Directions request failed: ' + status);
        }
    });
}

function computeJourneyDetails(result, titles) {
    let totalDurationSeconds = 0;
    let totalDistanceMeters = 0;

    let legs = result.routes[0].legs.map((leg, index) => {
        totalDurationSeconds += leg.duration.value;
        totalDistanceMeters += leg.distance.value;

        let legDurationHours = Math.round((leg.duration.value / 3600) * 100) / 100;
        let legDurationMinutes = Math.round(((leg.duration.value % 3600) / 60) * 100) / 100;
        let legDistanceKilometers = Math.round((leg.distance.value / 1000) * 100) / 100;

        // Extract the start and end location coordinates
        let startLocationCoordinates = {
            lat: leg.start_location.lat(),
            lng: leg.start_location.lng()
        };
        let endLocationCoordinates = {
            lat: leg.end_location.lat(),
            lng: leg.end_location.lng()
        };

        return {
            legNumber: index + 1,
            startLocation: leg.start_address,
            endLocation: leg.end_address,
            startTitle: titles[index],  // Include start title in the returned object
            endTitle: titles[index + 1] ? titles[index + 1] : null, // Include end title in the returned object
            startLocationCoordinates,  // include in the returned object
            endLocationCoordinates,    // include in the returned object
            durationHours: legDurationHours,
            durationMinutes: legDurationMinutes,
            distanceKilometers: legDistanceKilometers
        };
    });

    let totalDurationHours = Math.round((totalDurationSeconds / 3600) * 100) / 100;
    let totalDurationMinutes = Math.round(((totalDurationSeconds % 3600) / 60) * 100) / 100;
    let totalDistanceKilometers = Math.round((totalDistanceMeters / 1000) * 100) / 100;

    let journeyDetails = {
        totalDurationHours,
        totalDurationMinutes,
        totalDistanceKilometers,
        journeyDescription, // Include journey description in the returned object
        legs
    };

    console.log('After the computing the journey details: '  + '\n' + journeyDetails);
    return journeyDetails;
}


function displayJourneyDetails(journeyDetails) {
    let calculateDiv = document.getElementById('calculate');

    // Clear out the current contents
    calculateDiv.innerHTML = '';

    // Create elements to display journey description
    let journeyDescriptionElement = document.createElement('p');
    journeyDescriptionElement.textContent = journeyDetails.journeyDescription;

    // Create elements to display total duration and distance
    let totalDurationElement = document.createElement('p');
    totalDurationElement.textContent = 'Total Duration: ' + journeyDetails.totalDurationHours + ' hours ' + journeyDetails.totalDurationMinutes + ' minutes';
    let totalDistanceElement = document.createElement('p');
    totalDistanceElement.textContent = 'Total Distance: ' + journeyDetails.totalDistanceKilometers + ' kilometers';

    // Append these elements to the div
    calculateDiv.appendChild(journeyDescriptionElement);
    calculateDiv.appendChild(totalDurationElement);
    calculateDiv.appendChild(totalDistanceElement);

    // For each leg, create elements to display the leg's details
    journeyDetails.legs.forEach(leg => {
        let legElement = document.createElement('div');
        let legTitle = document.createElement('h3');
        let legStartEndTitle = document.createElement('p');
        let legStartEndLocation = document.createElement('p');
        let legDuration = document.createElement('p');
        let legDistance = document.createElement('p');

        legTitle.textContent = 'Leg ' + leg.legNumber;
        legStartEndTitle.textContent = leg.startTitle + ' to ' + leg.endTitle;
        legStartEndLocation.textContent = leg.startLocation + ' to ' + leg.endLocation;
        legDuration.textContent = 'Duration: ' + leg.durationHours + ' hours ' + leg.durationMinutes + ' minutes';
        legDistance.textContent = 'Distance: ' + leg.distanceKilometers + ' kilometers';

        legElement.appendChild(legTitle);
        legElement.appendChild(legStartEndTitle);
        legElement.appendChild(legStartEndLocation);
        legElement.appendChild(legDuration);
        legElement.appendChild(legDistance);

        calculateDiv.appendChild(legElement);
    });

    // Show the div
    calculateDiv.style.display = 'block';
}


function createUrlForGoogleMap(journeyDetails) {
    console.log("Creating the Url for Google Map!");
    // Construct the URL for Google Maps
    let googleMapsUrl = "https://www.google.com/maps/dir/";
    journeyDetails.legs.forEach((leg, index) => {
        // Use the latitude and longitude from startLocationCoordinates
        googleMapsUrl += `${leg.startLocationCoordinates.lat},${leg.startLocationCoordinates.lng}/`;
        if (index === journeyDetails.legs.length - 1) {
            // Add the destination (end location of the last leg) to the URL
            // Use the latitude and longitude from endLocationCoordinates
            googleMapsUrl += `${leg.endLocationCoordinates.lat},${leg.endLocationCoordinates.lng}`;
        }
    });

    // Get the "Display on Google Map" button
    let googleMapsButton = document.getElementById('displayOn-google');

    // Set the href attribute of the button to the Google Maps URL
    googleMapsButton.setAttribute('href', googleMapsUrl);

    console.log("Here is created url: " + googleMapsUrl);
    // Make the button visible
    googleMapsButton.style.display = "block";

    urlToGoGMap = googleMapsUrl; // Saving the URL to use in save button.
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
            stopsListContent += `<li draggable="true" ondragstart="drag(event)" id="${stopId}" data-stop-number="${globalCounter}" data-geocoded-address="${geocodedAddress}" title="${title}">${globalCounter}. ${stop}</li>`;
        }

        stopsListContent += '</ul>';
        stopsList.innerHTML += stopsListContent;
    }

    stopsList.style.display = stopsList.innerHTML ? 'block' : 'none'; //display only when it has content
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
function addPointToBar(id, text, isOriginDestination = false, geocodedAddress, title) {
    let spanElement = document.createElement('span');
    spanElement.setAttribute('class', 'journey-point');
    spanElement.setAttribute('id', id);
    spanElement.setAttribute('draggable', 'true');
    spanElement.setAttribute('data-geocoded-address', geocodedAddress);  // Set the geocoded address
    spanElement.setAttribute("ondragstart", "drag(event)");  // fire the "drag" event
    spanElement.textContent = text;
    spanElement.setAttribute('title', title);  // Set the title
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
    var data = ev.dataTransfer.getData("text"); //Name of the place
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
        //Get the title
        let title = draggedElement.getAttribute('title');
        console.log('Dropped title: ' + title);

        // Pass the original ID of the dragged element (which includes the timestamp), the text, and the geocoded address to addPointToBar
        addPointToBar(data, text, false, geocodedAddress, title);
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



///////////////////////////////////////////////////////////////////


    document.getElementById('journey-bar').addEventListener('drop', drop);
    document.getElementById('journey-bar').addEventListener('dragover', allowDrop);

    document.getElementById('send-button').addEventListener('click', handleButtonClick);

    var calculateButton = document.getElementById('calculate-button');

    calculateButton.addEventListener('click', calculateRoute);



function calculateRoute() {
    var journeyPoints = Array.from(document.getElementById('journey-bar').children);
    var route = journeyPoints.map(point => point.innerText);
    console.log('Calculating the route with the following stops: ', route);

    // Calculate the route with Google Maps API
    let { payload, titles } = createRouteWithStopPointsInOrder();

    // Call the function to create journey description
    journeyDescription = createJourneyDescription(titles);

    // Pass only payload to routeDirection
    routeDirection(payload, { titles }, displayUrl = true);
    //Display the save button after calculating the journey.
    document.getElementById('save-button').style.display = 'block';
}

// Function to create journey description
function createJourneyDescription(titles) {
    let description = '';

    // Get the names of the origin and destination points
    let origin = document.getElementById("origin").textContent;
    let destination = document.getElementById("destination").textContent;

    description += origin;

    for (let i = 0; i < titles.length; i++) {
        // Skip the title if it's the origin or destination
        if (titles[i] !== "Origin" && titles[i] !== "Destination") {
            description += ' to ' + titles[i];
        }
    }

    description += ' to ' + destination;

    console.log('Created the journey description: ' + '\n' + description);
    return description;
}



    //Getting all the elements in the journey bar and use them in the order they are displayed to create a payload
    function createRouteWithStopPointsInOrder() {
      let journeyPoints = document.querySelectorAll('.journey-point');
      let payload = {
        origin: null,
        destination: null,
        waypoints: [],
        travelMode: 'DRIVING'
      };
    let titles = [];

    journeyPoints.forEach((point, index) => {
        let address = point.getAttribute('data-geocoded-address');
        let title = point.getAttribute('title'); // get the title of the location
        titles.push(title);
        if (index === 0) { // The first element is the origin
            payload.origin = address;
        } else if (index === journeyPoints.length - 1) { // The last element is the destination
            payload.destination = address;
        } else { // The other elements are waypoints
            payload.waypoints.push({
                location: address,
                stopover: true
            });
        }
    });

    return { payload, titles };
}


    //Buttons to display and save the route
    var saveButton = document.getElementById('save-button');
    // Add event listeners for your new buttons
    saveButton.addEventListener('click', saveRoute);

document.getElementById('saveAndShare').addEventListener('click', sentJourneyToServer);

async function saveRoute() {
    document.getElementById('saveDialog').style.display = 'block';
}

async function sentJourneyToServer() {
    console.log("Save is invoked the, info: staticMapUrl, journeyDetails and urlToGoGMap is sending to server!!")
    // get the staticMapUrl
    var staticMapUrl = createStaticMapUrl(journeyDetails);

    // get the journey title entered by the user
    var journeyTitle = document.getElementById('title').value;

    // include geocoded origin and destination in journeyDetails
    journeyDetails.originLocation = {
        name: globalOriginName,
        geocodedAddress: globalGeocodedOrigin
    };
    journeyDetails.destinationLocation = {
        name: globalDestinationName,
        geocodedAddress: globalGeocodedDestination
    };


    // gather all data to send to server
    var dataToSend = {
        staticMapUrl: staticMapUrl,
        journeyDetails: journeyDetails,
        journeyTitle: journeyTitle,
        urlToGoGMap: urlToGoGMap
    };

    try {
        const response = await fetch('http://localhost:8082/api/jp/journey/create', {
            method: 'POST',
            headers: {
                'Authorization': 'Bearer ' + localStorage.getItem('accessToken'),
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(dataToSend)
        });

        const responseData = await response.text();
        console.log(responseData);

        // redirect the user to the communityRoom.html
        //window.location.href = "communityRoom.html";
    } catch (error) {
        console.error('Error:', error);
    }
}


function createStaticMapUrl(journeyDetails) {
    const apiKey = 'AIzaSyByDPWpC-sYKrLNvWjPd43qvdXWcTZKkDE';  // Replace with your own Google Maps API key
    const baseUrl = 'https://maps.googleapis.com/maps/api/staticmap?';

    let path = 'path=color:0x0000ff|weight:5';

    journeyDetails.legs.forEach((leg, index) => {
        // Use the latitude and longitude from startLocationCoordinates
        path += `|${leg.startLocationCoordinates.lat},${leg.startLocationCoordinates.lng}`;
        if (index === journeyDetails.legs.length - 1) {
            // Add the destination (end location of the last leg) to the path
            // Use the latitude and longitude from endLocationCoordinates
            path += `|${leg.endLocationCoordinates.lat},${leg.endLocationCoordinates.lng}`;
        }
    });

    // Define map size, in this case, 600x600
    let size = 'size=600x600';

    // Construct the full URL
    let staticMapUrl = `${baseUrl}${size}&${path}&key=${apiKey}`;

    console.log("Created the url to display: " + staticMapUrl)
    return staticMapUrl;
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

