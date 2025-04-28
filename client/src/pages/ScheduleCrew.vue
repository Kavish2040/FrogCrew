<template>
    <div class="page-container">
      <h1>Schedule Crew Members</h1>
  
      <div v-for="position in openPositions" :key="position.id" class="position-card">
        <h3>{{ position.role }} for {{ position.game }}</h3>
        <select v-model="assignments[position.id]" class="crew-dropdown">
          <option value="">Select Crew Member</option>
          <option v-for="member in crewList" :key="member.id" :value="member.name">
            {{ member.name }}
          </option>
        </select>
      </div>
  
      <button @click="handleScheduleCrew" class="btn-primary">Save Assignments</button>
    </div>
  </template>
  
  <script setup>
  import { ref } from 'vue';
  
  const openPositions = ref([
    { id: 1, role: 'Camera Operator', game: 'Game vs. Team A' },
    { id: 2, role: 'Replay Operator', game: 'Game vs. Team B' }
  ]);
  
  const crewList = ref([
    { id: 1, name: 'Sarah Smith' },
    { id: 2, name: 'Mike Evans' }
  ]);
  
  const assignments = ref({});
  
  const handleScheduleCrew = () => {
    console.log('Crew scheduled:', assignments.value);
    alert('Crew scheduled successfully!');
  };
  </script>
  
  <style scoped>
  .page-container {
    max-width: 800px;
    margin: 2rem auto;
    padding: 2rem;
  }
  
  .position-card {
    background: white;
    padding: 1.5rem;
    margin-bottom: 1.5rem;
    box-shadow: var(--shadow);
    border-radius: 1px;
  }
  
  .crew-dropdown {
    width: 100%;
    margin-top: 1rem;
    padding: 0.75rem;
    border: 1px solid #ccc;
    border-radius: 1px;
  }
  </style>
  