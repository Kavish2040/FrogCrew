<template>
    <div class="crewlist-page">
      <div class="page-header">
        <h1>Our Amazing Crew</h1>
        <p>Meet the team behind TCU’s game magic!</p>
      </div>
  
      <div class="crew-grid">
        <div v-for="member in crewMembers" :key="member.id" class="crew-card">
          <h2>{{ member.name }}</h2>
          <p><strong>Position:</strong> {{ member.position }}</p>
          <p><strong>Report:</strong> {{ member.reportTime }} at {{ member.reportLocation }}</p>
  
          <router-link :to="`/crew-profile/${member.id}`" class="btn-view">
            View Profile
          </router-link>
        </div>
      </div>
  
      <div class="back-link">
        <router-link to="/games" class="btn-back">← Back to Game Schedule</router-link>
      </div>
    </div>
  </template>
  
  <script setup>
  import { ref } from 'vue';
  import { useRoute } from 'vue-router';
  
  const route = useRoute();
  const gameId = route.params.id;
  
  // Temporary sample data
  const crewMembers = ref([
    { id: 1, name: 'Juan Hernandez', position: 'Camera Operator', reportTime: '5:00 PM', reportLocation: 'North Gate' },
    { id: 2, name: 'Sarah Smith', position: 'Audio Tech', reportTime: '5:15 PM', reportLocation: 'Main Entrance' },
    { id: 3, name: 'Mike Evans', position: 'Production Assistant', reportTime: '5:30 PM', reportLocation: 'South Gate' },
  ]);
  </script>
  
  <style scoped>
.crewlist-page {
  min-height: 100vh;
  padding: 3rem 2rem;
  background-color: var(--background-color);
}

.page-header {
  text-align: center;
  margin-bottom: 2rem;
}

.page-header h1 {
  font-size: 2.5rem;
  color: var(--tcu-purple);
  margin-bottom: 0.5rem;
}

.page-header p {
  color: var(--text-muted);
  font-size: 1.2rem;
}

.crew-grid {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(250px, 1fr));
  gap: 2rem;
  margin-top: 2rem;
}

.crew-card {
  background: white;
  padding: 1rem;
  border-radius: 1px;
  box-shadow: 0 2px 5px rgba(0, 0, 0, 0.08); /* 🔵 lighter shadow */
  text-align: center;
  transition: transform 0.3s ease, box-shadow 0.3s ease;
}

.crew-card:hover {
  transform: translateY(-2px);
  box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1);
}

.crew-card h2 {
  margin-bottom: 0.5rem;
  color: var(--tcu-purple);
  font-size: 1.4rem;
}

.crew-card p {
  margin-bottom: 0.75rem;
  color: var(--text-dark);
  font-size: 0.95rem;
}

.btn-view {
  display: inline-block;
  padding: 0.4rem 1rem;
  background-color: var(--tcu-purple);
  color: white;
  border-radius: 1px;
  font-weight: 600;
  text-decoration: none;
  font-size: 0.95rem;
  transition: background-color 0.3s ease;
}

.btn-view:hover {
  background-color: var(--tcu-dark-purple);
}

.back-link {
  margin-top: 3rem;
  text-align: center;
}

.btn-back {
  color: var(--tcu-purple);
  font-weight: 600;
  text-decoration: underline;
  transition: color 0.3s;
}

.btn-back:hover {
  color: var(--tcu-dark-purple);
}
</style>
