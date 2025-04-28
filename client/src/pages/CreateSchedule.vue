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
          <input id="season" type="text" v-model="form.season" placeholder="Spring 2025" required />
        </div>
  
        <button type="submit" class="btn-primary">Create Schedule</button>
      </form>
    </div>
  </template>
  
  <script setup>
  import { ref } from 'vue';
  
  const form = ref({
    sport: '',
    season: ''
  });
  
  const handleCreateSchedule = () => {
    console.log('Schedule Created:', form.value);
    alert('Game schedule created!');
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
  </style>
  