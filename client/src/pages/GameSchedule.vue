<template>
    <div class="gameschedule-container">
      <h1>Game Schedule</h1>
  
      <div class="filter-bar">
        <input type="text" v-model="search" placeholder="Search by opponent..." />
        <select v-model="sortBy">
          <option value="date">Sort by Date</option>
          <option value="venue">Sort by Venue</option>
        </select>
      </div>
  
      <div class="game-list">
        <div v-for="game in filteredGames" :key="game.id" class="game-card">
          <h2>{{ game.opponent }}</h2>
          <p><strong>Date:</strong> {{ game.date }}</p>
          <p><strong>Time:</strong> {{ game.time }}</p>
          <p><strong>Venue:</strong> {{ game.venue }}</p>
          <p><strong>Open Positions:</strong> {{ game.openPositions }}</p>
  
          <router-link :to="`/crewlist/${game.id}`" class="btn-primary">
            View Crew List
          </router-link>
        </div>
      </div>
    </div>
  </template>
  
  <script setup>
  import { ref, computed } from 'vue';
  
  const search = ref('');
  const sortBy = ref('date');
  
  // Sample game data - replace with API data later
  const games = ref([
    { id: 1, date: '2024-09-15', time: '7:00 PM', opponent: 'Baylor', venue: 'TCU Stadium', openPositions: 5 },
    { id: 2, date: '2024-09-22', time: '6:00 PM', opponent: 'Texas Tech', venue: 'TCU Arena', openPositions: 3 },
    { id: 3, date: '2024-10-01', time: '5:30 PM', opponent: 'UT Austin', venue: 'TCU Field', openPositions: 4 },
  ]);
  
  const filteredGames = computed(() => {
    let filtered = games.value.filter(game =>
      game.opponent.toLowerCase().includes(search.value.toLowerCase())
    );
  
    if (sortBy.value === 'date') {
      filtered.sort((a, b) => new Date(a.date) - new Date(b.date));
    } else if (sortBy.value === 'venue') {
      filtered.sort((a, b) => a.venue.localeCompare(b.venue));
    }
  
    return filtered;
  });
  </script>
  
  <style scoped>
  .gameschedule-container {
    padding: 2rem;
    max-width: 1000px;
    margin: 0 auto;
  }
  
  h1 {
    text-align: center;
    margin-bottom: 2rem;
    color: var(--tcu-purple);
  }
  
  .filter-bar {
    display: flex;
    gap: 1rem;
    margin-bottom: 2rem;
    justify-content: center;
  }
  
  .filter-bar input,
  .filter-bar select {
    padding: 0.75rem;
    border: 1px solid #ccc;
    border-radius: 1px;
    font-family: var(--font-base);
    width: 250px;
  }
  
  .game-list {
    display: grid;
    grid-template-columns: repeat(auto-fit, minmax(280px, 1fr));
    gap: 2rem;
  }
  
  .game-card {
    background: white;
    border-radius: 1px;
    box-shadow: var(--shadow);
    padding: 1.5rem;
    text-align: center;
  }
  
  .game-card h2 {
    color: var(--tcu-purple);
    margin-bottom: 1rem;
  }
  
  .game-card p {
    color: var(--text-muted);
    margin-bottom: 0.5rem;
  }
  
  .btn-primary {
    display: inline-block;
    margin-top: 1rem;
    padding: 0.6rem 1.2rem;
    background-color: var(--tcu-purple);
    color: white;
    border-radius: 1px;
    font-weight: 600;
    text-decoration: none;
    transition: background-color 0.3s ease;
  }
  
  .btn-primary:hover {
    background-color: var(--tcu-dark-purple);
  }
  </style>
  