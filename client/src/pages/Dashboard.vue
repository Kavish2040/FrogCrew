<template>
  <div class="dashboard-container">
    <aside class="sidebar">
      <div class="sidebar-header">
        <h2>FrogCrew</h2>
      </div>
      <nav class="sidebar-nav">
        <ul>
          <li>
            <router-link to="/dashboard">
              <span>Dashboard</span>
            </router-link>
          </li>
          <li>
            <router-link to="/crew">
              <span>Crew Members</span>
            </router-link>
          </li>
          <li>
            <router-link to="/games">
              <span>Games</span>
            </router-link>
          </li>
          <li v-if="userStore.isAdmin">
            <router-link to="/schedulecrew">
              <span>Schedule Crew</span>
            </router-link>
          </li>
          <li v-if="userStore.isAdmin">
            <router-link to="/createschedule">
              <span>Create Game Schedule</span>
            </router-link>
          </li>
          <li v-if="userStore.isAdmin">
            <router-link to="/addgame">
              <span>Add Games</span>
            </router-link>
          </li>
          <li v-if="userStore.isAdmin">
            <router-link to="/invite">
              <span>Invite Crew</span>
            </router-link>
          </li>
        </ul>
      </nav>
      <div class="sidebar-footer">
        <button @click="handleLogout" class="btn-logout">
          <span class="icon">🚪</span>
          <span>Logout</span>
        </button>
      </div>
    </aside>

    <main class="main-content">
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
    </main>
  </div>
</template>

<script setup>
import { useRouter } from 'vue-router';
import { useUserStore } from '../stores/userStore';

const router = useRouter();
const userStore = useUserStore();

const handleLogout = () => {
  userStore.logout();
  router.push('/login');
};
</script>

<style scoped>
.dashboard-container {
  display: flex;
  min-height: 100vh;
}

.sidebar {
  width: 250px;
  background-color: #2c3e50;
  color: white;
  display: flex;
  flex-direction: column;
}

.sidebar-header {
  padding: 1.5rem;
  border-bottom: 1px solid rgba(255, 255, 255, 0.1);
}

.sidebar-header h2 {
  margin: 0;
  font-size: 1.5rem;
  color: white;
}

.sidebar-nav {
  flex: 1;
  padding: 1rem 0;
}

.sidebar-nav ul {
  list-style: none;
  padding: 0;
  margin: 0;
}

.sidebar-nav li {
  margin-bottom: 0.25rem;
}

.sidebar-nav a {
  display: flex;
  align-items: center;
  padding: 0.75rem 1.5rem;
  color: rgba(255, 255, 255, 0.7);
  text-decoration: none;
  transition: all 0.3s;
}

.sidebar-nav a:hover, .sidebar-nav a.active {
  background-color: rgba(255, 255, 255, 0.1);
  color: white;
}

.sidebar-nav .icon {
  margin-right: 0.75rem;
  font-size: 1.2rem;
}

.sidebar-footer {
  padding: 1rem 1.5rem;
  border-top: 1px solid rgba(255, 255, 255, 0.1);
}

.btn-logout {
  display: flex;
  align-items: center;
  width: 100%;
  padding: 0.75rem;
  background-color: transparent;
  color: rgba(255, 255, 255, 0.7);
  border: 1px solid rgba(255, 255, 255, 0.2);
  border-radius: 1px;
  cursor: pointer;
  transition: all 0.3s;
}

.btn-logout:hover {
  background-color: rgba(255, 255, 255, 0.1);
  color: white;
}

.btn-logout .icon {
  margin-right: 0.75rem;
}

.main-content {
  flex: 1;
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

.empty-state {
  color: #999;
  font-style: italic;
  margin-bottom: 1rem;
}

.btn-action {
  padding: 0.6rem 1rem;
  background-color: #4F2683; /* TCU Purple */
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

.actions-list .btn-action {
  width: 100%;
}
</style> 