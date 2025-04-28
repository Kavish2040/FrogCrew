<template>
    <div class="page-container">
      <h1>Submit Availability</h1>
  
      <form @submit.prevent="handleSubmit" class="availability-form">
        <div v-for="game in games" :key="game.id" class="game-item">
          <label>
            <input type="checkbox" v-model="availability[game.id]" />
            {{ game.date }} - {{ game.opponent }} at {{ game.venue }}
          </label>
          <input
            type="text"
            v-model="comments[game.id]"
            placeholder="Optional comment"
            class="comment-field"
          />
        </div>
  
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
  
  const handleSubmit = () => {
    console.log('Submitting availability:', availability.value, comments.value);
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
    box-shadow: var(--shadow);
    border-radius: 1px;
  }
  
  .comment-field {
    width: 100%;
    margin-top: 0.5rem;
    padding: 0.5rem;
    border: 1px solid #ccc;
    border-radius: 1px;
  }
  </style>
  