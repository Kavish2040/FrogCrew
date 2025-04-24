<template>
    <div>
      <header class="main-header" v-if="route.path !== '/'">
        <router-link to="/" class="logo">FrogCrew</router-link>
        <nav>
          <router-link v-if="userStore.isLoggedIn" to="/dashboard">Dashboard</router-link>
          <router-link v-if="userStore.isLoggedIn" to="/crew">Crew</router-link>
          <router-link v-if="!userStore.isLoggedIn" to="/login">Login</router-link>
        </nav>
      </header>
  
      <main>
        <slot />
      </main>
  
      <footer class="main-footer">
        <p>&copy; {{ new Date().getFullYear() }} FrogCrew at TCU 🐸💜</p>
      </footer>
    </div>
  </template>
  
  <script setup>
  import { useRoute } from 'vue-router';
  import { useUserStore } from '../stores/userStore';
  
  const route = useRoute();
  const userStore = useUserStore();
  </script>
  
  <style scoped>
  .main-header {
    background-color: var(--tcu-purple);
    color: white;
    padding: 1rem 2rem;
    display: flex;
    justify-content: space-between;
    align-items: center;
    font-weight: 500;
    box-shadow: var(--shadow);
  }
  
  .logo {
    font-size: 1.5rem;
    color: white;
    font-weight: 600;
  }
  
  nav a {
    margin-left: 1rem;
    color: white;
  }
  
  nav a:hover {
    text-decoration: underline;
  }
  
  .main-footer {
    background-color: #eee;
    padding: 1rem;
    text-align: center;
    color: var(--text-muted);
    margin-top: 2rem;
  }
  </style>
  