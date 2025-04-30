<template>
  <div class="page-container">
    <h1>Create New Game Schedule</h1>

    <form @submit.prevent="handleCreateSchedule" class="schedule-form">
      <div class="form-group">
        <label for="sport">Sport</label>
        <input id="sport" v-model="form.sport" required />
      </div>

      <div class="form-group">
        <label for="season">Season</label>
        <input id="season" v-model="form.season" placeholder="e.g., Spring 2025" required />
      </div>

      <div class="form-group">
        <label for="gameDate">Game Date</label>
        <input id="gameDate" type="date" v-model="form.gameDate" required />
      </div>

      <div class="form-group">
        <label for="gameTime">Game Time</label>
        <input id="gameTime" type="time" v-model="form.gameTime" required />
      </div>

      <div class="form-group">
        <label for="venue">Venue</label>
        <input id="venue" v-model="form.venue" required />
      </div>

      <div class="form-group">
        <label for="opponent">Opponent</label>
        <input id="opponent" v-model="form.opponent" required />
      </div>

      <div class="form-group">
        <label for="positions">Required Crew Positions (comma-separated)</label>
        <input
          id="positions"
          v-model="form.positions"
          placeholder="e.g., Camera Operator, Audio, Director"
          required
        />
      </div>

      <div v-if="error" class="error-message">{{ error }}</div>

      <button type="submit" class="btn-primary">Create Schedule</button>
    </form>
  </div>
</template>

<script setup>
import { ref } from 'vue';

const form = ref({
  sport: '',
  season: '',
  gameDate: '',
  gameTime: '',
  venue: '',
  opponent: '',
  positions: ''
});

const error = ref('');

const handleCreateSchedule = () => {
  error.value = '';

  const fields = Object.values(form.value);
  const isEmpty = fields.some(field => !field.trim());

  if (isEmpty) {
    error.value = 'Please fill out all required fields.';
    return;
  }

  // Simulate saving to backend as a draft
  console.log('✅ Draft Game Schedule Created:', form.value);
  alert('Game schedule created and saved as draft!');
};
</script>

<style scoped>
.page-container {
  max-width: 600px;
  margin: 2rem auto;
  padding: 2rem;
}

.schedule-form {
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
  cursor: pointer;
  font-weight: 600;
}

.btn-primary:hover {
  background-color: #3a1c60;
}

.error-message {
  color: #e74c3c;
  font-weight: 500;
}
</style>
