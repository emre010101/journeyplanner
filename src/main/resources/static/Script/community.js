document.getElementById('search-btn').addEventListener('click', async function() { // <-- Note the async keyword here
    const url = 'http://localhost:8082/api/jp/journey/getJourneys';

    try {
        const response = await fetch(url, {
            headers: {
                'Authorization': 'Bearer ' + localStorage.getItem('accessToken'),
                'Content-Type': 'application/json'
            },
            method: 'GET'
        });

        const data = await response.json(); // or response.json() if you expect JSON response
        console.log(data);
        //displayJourneys(data.content);
    } catch (err) {
        console.error('Error:', err);
    }
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
