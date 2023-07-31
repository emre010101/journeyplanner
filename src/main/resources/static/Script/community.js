// Store parameters in an object to manage state
let searchParams = {
    sortBy: 'dateCreated',
    direction: 'asc',
    onlyUserJourneys: false,
    origin: null,
    destination: null,
    page: 0,
    size: 5
};
//The main content
let serverResponse;

// Define a function to fetch journeys with current search parameters
async function fetchJourneys() {
    // Construct URL with current search parameters
    const url = new URL('http://localhost:8082/api/jp/journey/getJourneys');
    // Print search params
    console.log("Printing the search params: ");
    console.log(searchParams);

    Object.keys(searchParams).forEach(key => url.searchParams.append(key, searchParams[key]));
    // Fetch data from API
    const response = await fetch(url, {
        headers: {
            'Authorization': 'Bearer ' + localStorage.getItem('accessToken'),
            'Content-Type': 'application/json'
        },
        method: 'GET'
    });
    if (!response.ok) {
        throw new Error(`HTTP error! status: ${response.status}`);
    }
    serverResponse = await response.json();
    console.log(serverResponse);
    // Update local storage with current search parameters
    localStorage.setItem('searchParams', JSON.stringify(searchParams));
    iterateJourneys(serverResponse);
    return serverResponse;
}

// Define a function to update search parameters and fetch journeys
function search() {
    // Update search parameters from inputs
    searchParams.onlyUserJourneys = document.getElementById('my-journey').checked;
    searchParams.direction = document.getElementById('sort-direction').value;
    searchParams.origin = document.getElementById('origin').value || null;
    searchParams.destination = document.getElementById('destination').value || null;
    searchParams.size = document.getElementById('page-size').value;
    searchParams.page = 0;  // reset page number to 0 when new search is made
    // Fetch journeys
    fetchJourneys();
}

// Define a function to fetch the next page of journeys
function nextPage() {
    searchParams.page++;
    fetchJourneys();
}
// Define a function to fetch the previous page of journeys
function previousPage() {
    if (searchParams.page > 0) {  // prevent going to negative page numbers
        searchParams.page--;
        fetchJourneys();
    }
}
// Add event listeners to buttons
document.getElementById('search-btn').addEventListener('click', search);
let nextButtons = document.getElementsByClassName('nav-btn nxt');
for(let i = 0; i < nextButtons.length; i++) {
    nextButtons[i].addEventListener('click', nextPage);
}
let prevButtons = document.getElementsByClassName('nav-btn pre');
for(let i = 0; i < prevButtons.length; i++) {
    prevButtons[i].addEventListener('click', previousPage);
}


// Load search parameters from local storage when page is loaded
window.onload = function() {
    const storedSearchParams = JSON.parse(localStorage.getItem('searchParams'));
    if (storedSearchParams) {
        searchParams = storedSearchParams;  // restore search parameters
    }
    // Fetch journeys and update UI
    fetchJourneys();
};



//////////////////////////////////////////////////////////////////////////////////

function iterateJourneys(){
    for(let i = 0; i < serverResponse.numberOfElements; i++) {
        let journeyContainer = createJourney(serverResponse.content[i]);
        journeysContainer.appendChild(journeyContainer);
    }
}
// Function to iterate through journeys and append them to the journeys container
function iterateJourneys(serverResponse) {
    // Get journeys container
    let journeysContainer = document.getElementById('journeys-container');

    // Clear any existing content in the journeys container
    journeysContainer.innerHTML = '';

    // Iterate through journeys in content
    for (let journey of serverResponse.content) {
        // Create journey container for each journey
        let journeyContainer = createJourneyContainer(journey);
        // Append journey container to journeys container
        journeysContainer.appendChild(journeyContainer);
    }
}


function createJourneyContainer(journey) {
    // Create a new div for the journey
    let journeyContainer = document.createElement('div');
    journeyContainer.className = 'journey-container';
    journeyContainer.id = 'journey-' + journey.id;

    // Create the general information section
    let generalInfo = document.createElement('div');
    generalInfo.className = 'general-info';

    // Add the journey title
    let title = document.createElement('h2');
    title.innerText = journey.journeyTitle;
    generalInfo.appendChild(title);

    // Add the creation date
    let dateCreated = document.createElement('p');
    dateCreated.innerText = journey.dateCreated;
    generalInfo.appendChild(dateCreated);

    // Add the user info
    let user = document.createElement('p');
    user.innerText = journey.userDTO.firstName + ' ' + journey.userDTO.lastName;
    generalInfo.appendChild(user);

    //Add legs count
    let countOfLegs = document.createElement('p');
    countOfLegs.innerText = "The number of legs in the journey: " + journey.journeyDetails.legs.length;

    generalInfo.appendChild(countOfLegs);

    // Append the general info to the journey container
    journeyContainer.appendChild(generalInfo);

    // TODO: Add other components like Journey Details, Image, and Buttons

    journeyContainer.appendChild(createJourneyDetailElement(journey));
    journeyContainer.appendChild(createJourneyImageElement(journey));
    journeyContainer.appendChild(createJourneyActionButtons(journey));

    // Return the journey container
    return journeyContainer;
}
function createJourneyDetailElement(journey) {
    // Create the journey details section
    let journeyDetails = document.createElement('div');
    journeyDetails.className = 'journey-details';

    // Add the total distance
    let totalDistance = document.createElement('p');
    totalDistance.innerText = 'Total Distance: ' + journey.journeyDetails.totalDistanceKilometers + ' km';
    journeyDetails.appendChild(totalDistance);

    // Add the total duration
    let totalDuration = document.createElement('p');
    totalDuration.innerText = 'Total Duration: ' + journey.journeyDetails.totalDurationHours + ' hours';
    journeyDetails.appendChild(totalDuration);

    // TODO: Add other details

    // Create a button to show/hide the journey details
    let detailsButton = document.createElement('button');
    detailsButton.innerText = 'See More Details';
    detailsButton.onclick = function() {
        // Toggle the visibility of the journey details
        if (journeyDetails.style.display === 'none') {
            journeyDetails.style.display = 'block';
        } else {
            journeyDetails.style.display = 'none';
        }
    };
    journeyDetails.appendChild(detailsButton);

    return journeyDetails;
}

// Function to create journey image element
function createJourneyImageElement(journey) {
    let journeyImageContainer = document.createElement('div');
    journeyImageContainer.className = 'journey-image-container';

    let journeyImage = document.createElement('img');
    journeyImage.src = journey.staticMapUrl;
    journeyImageContainer.appendChild(journeyImage);

    return journeyImageContainer;
}
// Function to create journey action buttons
function createJourneyActionButtons(journey) {
    let journeyActionsContainer = document.createElement('div');
    journeyActionsContainer.className = 'journey-actions-container';

    let likeButton = document.createElement('button');
    likeButton.innerText = 'Like';
    journeyActionsContainer.appendChild(likeButton);

    let commentButton = document.createElement('button');
    commentButton.innerText = 'Comment';
    journeyActionsContainer.appendChild(commentButton);

    if (journey.userJourney) {
        let deleteButton = document.createElement('button');
        deleteButton.innerText = 'Delete Journey';
        journeyActionsContainer.appendChild(deleteButton);
    }

    let displayOnGoogle = document.createElement('a');
    displayOnGoogle.className = 'button';
    displayOnGoogle.setAttribute('href', journey.urlToGoGMap);
    displayOnGoogle,innerText = "Display on Google";
    journeyActionsContainer.appendChild(displayOnGoogle);

   /* var mydiv = document.getElementById("myDiv");
    var aTag = document.createElement('a');
    aTag.setAttribute('href',"yourlink.htm");
    aTag.innerText = "link text";
    mydiv.appendChild(aTag);*/


    return journeyActionsContainer;
}


function displayJourneys(journeys) {
    const container = document.getElementById('journeys-container');
    container.innerHTML = '';  // clear existing journeys

    journeys.forEach(journey => {
        const journeyDiv = document.createElement('div');
        journeyDiv.textContent = JSON.stringify(journey);  // simplistic display, replace with how you want to display the journeys
        container.appendChild(journeyDiv);
    });
}
