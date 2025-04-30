<template>
  <div class="page-container">
    <h1>Submit Availability</h1>

    <form @submit.prevent="handleSubmit" class="availability-form">
      <div
        v-for="game in games"
        :key="game.id"
        class="game-item"
      >
        <label>
          <input
            type="checkbox"
            v-model="availability[game.id]"
          />
          {{ game.date }} - {{ game.opponent }} at {{ game.venue }}
        </label>
        <input
          type="text"
          v-model="comments[game.id]"
          placeholder="Optional comment"
          class="comment-field"
        />
      </div>

      <div v-if="error" class="error-message">{{ error }}</div>

      <button type="submit" class="btn-primary">Submit Availability</button>
    </form>
  </div>
</template>

<script setup>
import { ref } from 'vue';

const games = ref([
  { id: 1, date: '2025-05-01', opponent: 'Team A', venue: 'Stadium 1' },
  { id: 2, date: '2025-05-08', opponent: 'Team B', venue: 'Stadium 2' }
]);

const availability = ref({});
const comments = ref({});
const error = ref('');

const handleSubmit = () => {
  const selectedGames = Object.keys(availability.value).filter(
    id => availability.value[id]
  );

  if (selectedGames.length === 0) {
    error.value = 'Please select at least one game you are available for.';
    return;
  }

  error.value = ''; // Clear any previous error

  // Simulated save
  console.log('Saved availability:', availability.value);
  console.log('Comments:', comments.value);

  // Simulated notification to Admin
  console.log('Admin notified of new availability submission.');

  alert('Availability submitted successfully!');
};
</script>

<style scoped>
.page-container {
  max-width: 800px;
  margin: 2rem auto;
  padding: 2rem;
}

.availability-form {
  display: flex;
  flex-direction: column;
  gap: 1.5rem;
}

.game-item {
  background: white;
  padding: 1rem;
  box-shadow: 0 2px 5px rgba(0,0,0,0.05);
  border-radius: 1px;
}

.comment-field {
  width: 100%;
  margin-top: 0.5rem;
  padding: 0.5rem;
  border: 1px solid #ccc;
  border-radius: 1px;
}

.error-message {
  color: #e74c3c;
  font-weight: 500;
  margin-bottom: 1rem;
}
</style>
 