document.getElementById('search-btn').addEventListener('click', function() {
    const myJourney = document.getElementById('my-journey').checked;
    let origin = document.getElementById('origin').value;
    let destination = document.getElementById('destination').value;
    const sortDirection = document.getElementById('sort-direction').value;
    const pageSize = document.getElementById('page-size').value;
    let userEmail = '';

    if(myJourney) {
        userEmail = localStorage.getItem('userEmail');
    }

    if(origin === '') {
        origin = null;
    }
    if(destination === '') {
        destination = null;
    }

    const url = new URL('http://localhost:8082/journey/journeys');
    const params = {
        userEmail: myJourney ? userEmail : undefined,
        sortBy: 'dateCreated',
        direction: sortDirection,
        origin: origin,
        destination: destination,
        page: 0,
        size: pageSize
    };
    Object.keys(params).forEach(key => url.searchParams.append(key, params[key]));

    fetch(url, {
            headers: {
                'Authorization': 'Bearer ' + localStorage.getItem('accessToken'),
                'Content-Type': 'application/json'
            },
            method: 'GET'
        })
        .then(response => response.json())
        .then(data => {
            console.log(data);
            //displayJourneys(data.content);
        })
        .catch(err => console.error('Error:', err));
});

function displayJourneys(journeys) {
    const container = document.getElementById('journeys-container');
    container.innerHTML = '';  // clear existing journeys

    journeys.forEach(journey => {
        const journeyDiv = document.createElement('div');
        journeyDiv.textContent = JSON.stringify(journey);  // simplistic display, replace with how you want to display the journeys
        container.appendChild(journeyDiv);
    });
}
