<template>
  <div class="layout-container">
    <aside :class="['sidebar', { collapsed }]">
      <div class="toggle-btn" @click="collapsed = !collapsed">
        <span>{{ collapsed ? '➡' : '⬅' }}</span>
      </div>
      <div class="sidebar-header" v-if="!collapsed">
        <router-link to="/dashboard" class="logo">FrogCrew</router-link>
      </div>
      <nav class="sidebar-nav">
        <ul>
          <li>
            <router-link to="/dashboard">
              <span v-if="!collapsed">Dashboard</span>
              <span v-else title="Dashboard"></span>
            </router-link>
          </li>
          <li>
            <router-link to="/crew">
              <span v-if="!collapsed">Crew</span>
              <span v-else title="Crew"></span>
            </router-link>
          </li>
          <li>
            <router-link to="/games">
              <span v-if="!collapsed">Games</span>
              <span v-else title="Games"></span>
            </router-link>
          </li>
          <li v-if="isAdmin">
            <router-link to="/invite">
              <span v-if="!collapsed">Invite Crew</span>
              <span v-else title="Invite"></span>
            </router-link>
          </li>
        </ul>
      </nav>
    </aside>

    <div class="content-wrapper">
      <header class="main-header" v-if="route.path !== '/'">
        <router-link to="/dashboard" class="logo">FrogCrew</router-link>
        <nav>
          <router-link v-if="isLoggedIn" to="/dashboard">Dashboard</router-link>

          <button
            v-if="isLoggedIn"
            @click="handleLogout"
            class="btn-logout-top"
          >
            Logout
          </button>

          <router-link v-else to="/login">Login</router-link>
        </nav>
      </header>

      <main class="main-content">
        <slot />
      </main>

      <footer class="main-footer">
        <p>&copy; {{ new Date().getFullYear() }} FrogCrew at TCU 🐸</p>
      </footer>
    </div>
  </div>
</template>

<script setup>
import { useRoute, useRouter } from 'vue-router';
import { ref, computed } from 'vue';
import { useUserStore } from '../stores/userStore';

const route = useRoute();
const router = useRouter();
const collapsed = ref(false);
const userStore = useUserStore();

const isLoggedIn = computed(() => userStore.isLoggedIn);
const isAdmin = computed(() => userStore.isAdmin);

const handleLogout = () => {
  userStore.logout();
  router.push('/login');
};
</script>

<style scoped>
:root {
  --bg-light-purple: #f7f4fa;
}

.layout-container {
  display: flex;
  min-height: 100vh;
}

.sidebar {
  background-color: #2c3e50;
  color: white;
  width: 250px;
  transition: width 0.3s ease;
  position: relative;
}

.sidebar.collapsed {
  width: 60px;
}

.toggle-btn {
  position: absolute;
  top: 1rem;
  right: -1.2rem;
  background: #2c3e50;
  color: white;
  padding: 0.3rem 0.6rem;
  border-radius: 0 4px 4px 0;
  cursor: pointer;
  z-index: 1000;
  font-weight: bold;
}

.sidebar-header {
  padding: 1.5rem 1rem;
  border-bottom: 1px solid rgba(255, 255, 255, 0.1);
}

.sidebar-header .logo {
  font-size: 1.3rem;
  font-weight: bold;
  color: white;
}

.sidebar-nav ul {
  list-style: none;
  padding: 1rem;
  margin: 0;
}

.sidebar-nav li {
  margin-bottom: 0.5rem;
}

.sidebar-nav a {
  color: rgba(255, 255, 255, 0.8);
  text-decoration: none;
  display: flex;
  align-items: center;
  padding: 0.5rem 1rem;
  border-radius: 4px;
  transition: background 0.2s;
  white-space: nowrap;
  overflow: hidden;
  text-overflow: ellipsis;
}

.sidebar-nav a:hover,
.sidebar-nav a.router-link-exact-active {
  background-color: rgba(255, 255, 255, 0.15);
  color: white;
}

.content-wrapper {
  flex: 1;
  display: flex;
  flex-direction: column;
  background: var(--bg-light-purple);
}

.main-header {
  background-color: #4f2683;
  color: white;
  padding: 1rem 2rem;
  display: flex;
  justify-content: space-between;
  align-items: center;
  font-weight: 500;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.05);
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

.btn-logout-top {
  background: none;
  border: none;
  color: white;
  font-weight: 600;
  margin-left: 1rem;
  cursor: pointer;
  font-size: 1rem;
}

.btn-logout-top:hover {
  text-decoration: underline;
}

.main-content {
  flex: 1;
  padding: 2rem;
}

.main-footer {
  background-color: #eee;
  padding: 1rem;
  text-align: center;
  color: #666;
}
</style>
