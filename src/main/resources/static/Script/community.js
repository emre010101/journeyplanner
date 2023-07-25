fetch('/api/journeys')
  .then(response => response.json())
  .then(journeys => {
    const container = document.querySelector('.journey-container');

    journeys.forEach(journey => {
      // Create journey row
      const row = document.createElement('div');
      row.className = 'journey-row';

      // Create title
      const title = document.createElement('h2');
      title.textContent = journey.title;
      row.appendChild(title);

      // Create user email
      const user = document.createElement('p');
      user.textContent = journey.userEmail;  // Replace with actual user email
      row.appendChild(user);

      // Create date
      const date = document.createElement('p');
      date.textContent = journey.createdDate;  // Replace with actual created date
      row.appendChild(date);

      // Create details
      const details = document.createElement('p');
      details.textContent = journey.journeyDetails;
      row.appendChild(details);

      // Create details expansion
      const moreDetails = document.createElement('details');
      const summary = document.createElement('summary');
      summary.textContent = 'View More Details';
      moreDetails.appendChild(summary);

      const legs = document.createElement('p');
      legs.textContent = 'Journey Legs Details';  // Replace with actual journey leg details
      moreDetails.appendChild(legs);

      row.appendChild(moreDetails);

      // Create image
      const img = document.createElement('img');
      img.src = journey.imageUrl;
      img.alt = 'journey image';
      row.appendChild(img);

      // Create action buttons
      const actions = document.createElement('div');
      actions.className = 'journey-actions';

      const likes = document.createElement('div');
      likes.className = 'likes';

      const likeCount = document.createElement('span');
      likeCount.textContent = journey.likeCount;  // Replace with actual like count
      likes.appendChild(likeCount);

      likes.appendChild(document.createTextNode(' Likes'));

      const likeButton = document.createElement('button');
      likeButton.textContent = 'Like';
      likes.appendChild(likeButton);

      actions.appendChild(likes);

      const comments = document.createElement('details');
      const commentsSummary = document.createElement('summary');
      commentsSummary.textContent = 'Comments';
      comments.appendChild(commentsSummary);

      const commentsSection = document.createElement('div');
      commentsSection.id = 'comments-section';  // You will populate this with actual comments later
      comments.appendChild(commentsSection);

      const commentButton = document.createElement('button');
      commentButton.textContent = 'Comment';
      comments.appendChild(commentButton);

      actions.appendChild(comments);

      row.appendChild(actions);

      // Append row to container
      container.appendChild(row);
    });
  });

function openGoogleMap(mapUrl) {
  window.open(mapUrl, '_blank');
}
