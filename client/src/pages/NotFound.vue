<template>
  <div class="not-found-container">
    <div class="not-found-content">
      <h1>404</h1>
      <h2>Page Not Found</h2>
      <p>The page you are looking for doesn't exist or has been moved.</p>
      <router-link to="/" class="btn-home">Go to Home</router-link>
    </div>
  </div>
</template>

<script setup>
// Not Found page component logic
</script>

<style scoped>
.not-found-container {
  display: flex;
  justify-content: center;
  align-items: center;
  min-height: 100vh;
  background-color: #f5f5f5;
  padding: 1rem;
}

.not-found-content {
  text-align: center;
  padding: 3rem;
  background-color: white;
  border-radius: 8px;
  box-shadow: 0 4px 10px rgba(0, 0, 0, 0.1);
  max-width: 500px;
}

h1 {
  font-size: 6rem;
  margin: 0;
  color: #4F2683; /* TCU Purple */
  line-height: 1;
}

h2 {
  font-size: 2rem;
  margin: 0.5rem 0 1.5rem;
  color: #333;
}

p {
  color: #666;
  margin-bottom: 2rem;
}

.btn-home {
  display: inline-block;
  padding: 0.75rem 1.5rem;
  background-color: #4F2683; /* TCU Purple */
  color: white;
  text-decoration: none;
  border-radius: 4px;
  font-weight: 500;
  transition: background-color 0.3s;
}

.btn-home:hover {
  background-color: #3a1c60;
}
</style> 