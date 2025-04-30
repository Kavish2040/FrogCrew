<template>
  <div class="dashboard-page">
    <header class="content-header">
      <h1>Dashboard</h1>
      <div class="user-info">
        <span>Welcome, {{ userStore.userName }}</span>
      </div>
    </header>

    <div class="dashboard-grid">
      <!-- Card for Games -->
      <div class="dashboard-card">
        <h3>Upcoming Games</h3>
        <div class="card-content">
          <router-link to="/games" class="btn-action">View Games</router-link>
          <router-link v-if="userStore.isAdmin" to="/addgame" class="btn-action">Add Game</router-link>
        </div>
      </div>

      <!-- Card for Crew -->
      <div class="dashboard-card">
        <h3>Crew Members</h3>
        <div class="card-content">
          <router-link to="/crew" class="btn-action">View Crew List</router-link>
          <router-link v-if="userStore.isAdmin" to="/invite" class="btn-action">Invite Crew Member</router-link>
        </div>
      </div>

      <!-- Card for Availability -->
      <div class="dashboard-card">
        <h3>Availability</h3>
        <div class="card-content">
          <router-link to="/availability" class="btn-action">Submit Availability</router-link>
        </div>
      </div>

      <!-- Admin Only Card -->
      <div class="dashboard-card" v-if="userStore.isAdmin">
        <h3>Admin Actions</h3>
        <div class="card-content actions-list">
          <router-link to="/createschedule" class="btn-action">Create Game Schedule</router-link>
          <router-link to="/schedulecrew" class="btn-action">Schedule Crew</router-link>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { useUserStore } from '../stores/userStore';

const userStore = useUserStore();
</script>

<style scoped>
.dashboard-page {
  padding: 2rem;
}

.content-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 2rem;
}

.content-header h1 {
  margin: 0;
  color: #333;
  font-size: 1.8rem;
}

.user-info {
  margin-left: 2em;
  color: #666;
}

.dashboard-grid {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(300px, 1fr));
  gap: 1.5rem;
}

.dashboard-card {
  background-color: white;
  border-radius: 1px;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.05);
  overflow: hidden;
}

.dashboard-card h3 {
  margin: 0;
  padding: 1rem 1.5rem;
  background-color: #4F2683; /* TCU Purple */
  color: white;
  font-size: 1.1rem;
}

.card-content {
  padding: 1.5rem;
  min-height: 150px;
  display: flex;
  flex-direction: column;
  justify-content: space-between;
}

.btn-action {
  padding: 0.6rem 1rem;
  background-color: #4F2683;
  color: white;
  border: none;
  border-radius: 1px;
  cursor: pointer;
  transition: background-color 0.3s;
  font-weight: 500;
  align-self: flex-start;
}

.btn-action:hover {
  background-color: #3a1c60;
}

.actions-list {
  display: flex;
  flex-direction: column;
  gap: 0.75rem;
}
</style>
