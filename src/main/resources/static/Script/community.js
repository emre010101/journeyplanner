// Store parameters in an object to manage state
let searchParams = {
    sortBy: 'dateCreated',
    direction: 'desc',
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
document.getElementById('to-top').addEventListener('click', function() {
    window.scrollTo(0, 0);
});


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

    // Append the general info to the journey container
    journeyContainer.appendChild(createNameDateTitle(journey));
    journeyContainer.appendChild(createGeneralJourneyDetails(journey));
    //Append all the details of the legs
    journeyContainer.appendChild(createJourneyLegsDetail(journey));
    //Display the imagine which represent the trip
    journeyContainer.appendChild(createJourneyImageElement(journey));
    journeyContainer.appendChild(createJourneyActionButtons(journey));
    let commentSection = createCommentSection(journey);
    journeyContainer.appendChild(commentSection);

    // Return the journey container
    return journeyContainer;
}

function createNameDateTitle(journey){
    // Create the general information section
    let generalInfo = document.createElement('div');
    generalInfo.className = 'general-info';
    // Add the journey title
    let title = document.createElement('h2');
    title.innerText = journey.journeyTitle;
    generalInfo.appendChild(title);
    // Add the creation date
    let dateCreated = document.createElement('p');
    dateCreated.setAttribute('class', 'date');
    dateCreated.innerText = journey.dateCreated;
    generalInfo.appendChild(dateCreated);
    // Add the user info
    let user = document.createElement('p');
    user.innerText = journey.userDTO.firstName + ' ' + journey.userDTO.lastName;
    generalInfo.appendChild(user);
    return generalInfo;
}
function createGeneralJourneyDetails(journey){
    // Create the journey details section
    let journeyDetails = document.createElement('div');
    journeyDetails.className = 'journey-details';
    //Add legs count
    let countOfLegs = document.createElement('p');
    countOfLegs.innerText = "The number of legs in the journey: " + journey.journeyDetails.legs.length;
    journeyDetails.appendChild(countOfLegs);
    // Add the total distance
    let totalDistance = document.createElement('p');
    totalDistance.innerText = 'Total Distance: ' + journey.journeyDetails.totalDistanceKilometers + ' km';
    journeyDetails.appendChild(totalDistance);
    // Add the total duration
    let totalDuration = document.createElement('p');
    totalDuration.innerText = 'Total Duration: ' + journey.journeyDetails.totalDurationHours + ' hours';
    journeyDetails.appendChild(totalDuration);
    return journeyDetails;
}
function createJourneyLegsDetail(journey) {
    // Create a main container for the journey details
    let journeyDetailMainContainer = document.createElement('div');
    journeyDetailMainContainer.className = 'journey-leg-container';

    // Create a container for the journey details
    let journeyDetailsContainer = document.createElement('div');
    journeyDetailsContainer.className = 'journey-leg';
    journeyDetailsContainer.style.display = 'none';  // Initially hide the details

    // Create a button to show/hide the journey details
    let detailsButton = document.createElement('button');
    detailsButton.innerText = 'See More Details';
    detailsButton.onclick = function() {
        // Toggle the visibility of the journey details
        if (journeyDetailsContainer.style.display === 'none') {
            journeyDetailsContainer.style.display = 'block';
        } else {
            journeyDetailsContainer.style.display = 'none';
        }
    };

    // Append the button to the main container
    journeyDetailMainContainer.appendChild(detailsButton);

    // Create a new div for each leg in the journey
    for (let leg of journey.journeyDetails.legs) {
        let legContainer = document.createElement('div');

        // Add the details for the leg to the leg container
        legContainer.innerHTML = `
            <h3>Leg ${leg.legNumber}</h3>
            <p>Titles: ${leg.startTitle} to ${leg.endTitle}</p>
            <p>Start Location: ${leg.startLocation}</p>
            <p>End Location: ${leg.endLocation}</p>
            <p>Distance: ${leg.distanceKilometers} km</p>
            <p>Duration: ${leg.durationHours} hours</p>
        `;

        // Append the leg container to the journey details container
        journeyDetailsContainer.appendChild(legContainer);
    }

    // Append the details container to the main container
    journeyDetailMainContainer.appendChild(journeyDetailsContainer);

    return journeyDetailMainContainer;
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

function createJourneyActionButtons(journey) {
    let journeyActionsContainer = document.createElement('div');
    journeyActionsContainer.className = 'journey-actions-container';

    journeyActionsContainer.appendChild(createLikeButton(journey));
    journeyActionsContainer.appendChild(createDisplayOnGoogleButton(journey));
    if (journey.userJourney) {
        journeyActionsContainer.appendChild(createDeleteButton(journey));
    }
    journeyActionsContainer.appendChild(createCommentButton(journey));

    return journeyActionsContainer;
}

function createLikeButton(journey) {
    let likeButton = document.createElement('button');
    let likeCount = document.createElement('span');

    likeButton.className = journey.isUserLike ? 'like-button liked' : 'like-button';
    likeButton.innerHTML = journey.isUserLike ? '❤️' : '🤍';
    likeButton.onclick = function() {
        // Toggle like status and update the button and count
        journey.isUserLike = !journey.isUserLike;
        likeButton.className = journey.isUserLike ? 'like-button liked' : 'like-button';
        likeButton.innerHTML = journey.isUserLike ? '❤️' : '🤍';
        likeCount.innerText = journey.likesCount;  // Getting the count
    };
    likeCount.className = 'like-count';
    likeCount.innerText = journey.likesCount;

    let likeContainer = document.createElement('div');
    likeContainer.appendChild(likeButton);
    likeContainer.appendChild(likeCount);

    return likeContainer;
}

function createCommentButton(journey) {
    let commentButton = document.createElement('button');
    commentButton.innerText = 'Comment';
    commentButton.onclick = function() {
        // TODO: Open comment box
    };

    let commentCount = document.createElement('span');
    commentCount.innerText = journey.commentsCount;  // Assume 'comments' is the list of comments

    let commentContainer = document.createElement('div');
    commentContainer.appendChild(commentButton);
    commentContainer.appendChild(commentCount);

    return commentContainer;
}

function createDeleteButton(journey) {
    let deleteButton = document.createElement('button');
    deleteButton.innerText = 'Delete Journey';
    deleteButton.onclick = function() {
        // TODO: Delete the journey
    };

    return deleteButton;
}

function createDisplayOnGoogleButton(journey) {
    let displayOnGoogle = document.createElement('a');
    displayOnGoogle.className = 'display-button';
    displayOnGoogle.setAttribute('href', journey.urlToGoGMap);
    displayOnGoogle.setAttribute('target', '_blank');
    displayOnGoogle.innerText = "Display on Google";

    return displayOnGoogle;
}

function createCommentSection(journey) {
    let commentSection = document.createElement('div');
    commentSection.className = 'comment-section';
    commentSection.id = 'comment-section-' + journey.comments.id;  // Use the journey ID as part of the comment section ID

    let allCommentsContainer = document.createElement('div');
    allCommentsContainer.className = 'all-comments';
    allCommentsContainer.style.display = 'none';  // Initially, hide all comments

    journey.comments.forEach((comment) => {
        let commentDiv = createCommentDiv(comment);  // Function to create a div for each comment
        allCommentsContainer.appendChild(commentDiv);
    });

    let showMoreButton = document.createElement('button');
    showMoreButton.innerText = 'Show More Comments';
    showMoreButton.onclick = function() {
        if (allCommentsContainer.style.display === 'none') {
            allCommentsContainer.style.display = 'block';
            showMoreButton.innerText = 'Show Comments';
        } else {
            allCommentsContainer.style.display = 'none';
            showMoreButton.innerText = 'Close Comments';
        }
    };
    commentSection.appendChild(showMoreButton);

    commentSection.appendChild(allCommentsContainer);

    return commentSection;
}

function createCommentDiv(comment) {
    let commentDiv = document.createElement('div');
    commentDiv.className = 'comment';

    let commentContent = document.createElement('p');
    commentContent.innerText = comment.content;

    let commentUser = document.createElement('span');
    commentUser.innerText = comment.userDTO.firstName + ' ' + comment.userDTO.lastName;

    let commentDate = document.createElement('span');
    commentDate.innerText = comment.updatedDate ? `Updated: ${comment.updatedDate}` : `Created: ${comment.createdDate}`;

    commentDiv.appendChild(commentContent);
    commentDiv.appendChild(commentUser);
    commentDiv.appendChild(commentDate);

    if (comment.userComment) {
        let deleteButton = document.createElement('button');
        deleteButton.innerText = 'Delete Comment';
        deleteButton.onclick = function() {
            // TODO: Delete the comment
        };

        let updateButton = document.createElement('button');
        updateButton.innerText = 'Update Comment';
        updateButton.onclick = function() {
            // TODO: Update the comment
        };

        commentDiv.appendChild(deleteButton);
        commentDiv.appendChild(updateButton);
    }

    return commentDiv;
}


