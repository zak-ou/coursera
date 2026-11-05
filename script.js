/**
 * addRecommendation
 * -----------------
 * Reads the text from the recommendation textarea.
 * If the input is not empty, adds a new recommendation card to the list
 * and then calls showPopup(true) to display the confirmation popup.
 * showPopup is ONLY called after a recommendation has been successfully submitted.
 */
function addRecommendation() {
  const input = document.getElementById('rec-input');
  const text = input.value.trim();

  // Guard: do nothing (and do NOT show popup) if the field is empty
  if (!text) {
    input.focus();
    input.style.borderColor = '#e74c3c';
    setTimeout(() => {
      input.style.borderColor = '#d0d7f0';
    }, 1500);
    return;
  }

  // Create a new recommendation card
  const card = document.createElement('div');
  card.classList.add('recommendation-card', 'recommendation');

  const recText = document.createElement('p');
  recText.classList.add('rec-text');
  recText.textContent = `"${text}"`;

  const recAuthor = document.createElement('p');
  recAuthor.classList.add('rec-author');
  recAuthor.textContent = '— Anonymous Visitor';

  card.appendChild(recText);
  card.appendChild(recAuthor);

  // Append the new card to the recommendations list
  const list = document.getElementById('recommendations-list');
  list.appendChild(card);

  // Clear the textarea
  input.value = '';

  // Show the popup ONLY after a successful submission
  showPopup(true);
}

/**
 * showPopup
 * ---------
 * Displays the confirmation popup overlay.
 * Accepts an optional parameter (true) to confirm it was triggered by a submission.
 * This function is called exclusively from addRecommendation(),
 * ensuring it only runs after a recommendation has been submitted.
 */
function showPopup(submitted) {
  if (!submitted) return;
  const popup = document.getElementById('popup');
  popup.classList.remove('hidden');
}

/**
 * closePopup
 * ----------
 * Hides the confirmation popup overlay.
 * Called when the user clicks the "Close" button inside the popup.
 */
function closePopup() {
  const popup = document.getElementById('popup');
  popup.classList.add('hidden');
}
