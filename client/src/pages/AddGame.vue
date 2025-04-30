<template>
  <div class="page-container">
    <h1>Add Game</h1>

    <form @submit.prevent="handleAddGame" class="addgame-form">
      <div class="form-group">
        <label for="opponent">Opponent</label>
        <input id="opponent" v-model="game.opponent" required />
      </div>

      <div class="form-group">
        <label for="venue">Venue</label>
        <input id="venue" v-model="game.venue" required />
      </div>

      <div class="form-group">
        <label for="date">Date</label>
        <input id="date" type="date" v-model="game.date" required />
      </div>

      <div class="form-group">
        <label for="time">Time</label>
        <input id="time" type="time" v-model="game.time" required />
      </div>

      <div class="form-group">
        <label for="positions">Required Crew Positions (comma-separated)</label>
        <input
          id="positions"
          v-model="game.positions"
          placeholder="e.g., Camera, Director, Audio"
          required
        />
      </div>

      <div v-if="error" class="error-message">{{ error }}</div>

      <button type="submit" class="btn-primary">Add Game</button>
    </form>
  </div>
</template>

<script setup>
import { ref } from 'vue';

const game = ref({
  opponent: '',
  venue: '',
  date: '',
  time: '',
  positions: ''
});

const error = ref('');

const handleAddGame = () => {
  error.value = '';

  const fields = Object.entries(game.value);
  const missing = fields.find(([, val]) => !val.trim());

  if (missing) {
    error.value = '❗ Please fill out all required fields.';
    return;
  }

  // Simulate saving the game
  console.log('✅ Game added:', game.value);
  alert('Game added successfully!');
};
</script>

<style scoped>
.page-container {
  max-width: 600px;
  margin: 2rem auto;
  padding: 2rem;
}

.addgame-form {
  display: flex;
  flex-direction: column;
  gap: 1.5rem;
}

.form-group label {
  font-weight: bold;
}

input {
  padding: 0.75rem;
  border: 1px solid #ccc;
  border-radius: 1px;
  width: 100%;
}

.btn-primary {
  background-color: #4F2683;
  color: white;
  padding: 0.75rem;
  border: none;
  border-radius: 1px;
  font-weight: 600;
  cursor: pointer;
}

.btn-primary:hover {
  background-color: #3a1c60;
}

.error-message {
  color: #e74c3c;
  font-weight: 500;
}
</style>
