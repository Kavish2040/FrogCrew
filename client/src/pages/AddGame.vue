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
    time: ''
  });
  
  const handleAddGame = () => {
    console.log('Game Added:', game.value);
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
  </style>
  