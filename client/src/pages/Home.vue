<template>
  <div class="home-container">
    <header class="hero">
      <h1>Welcome to FrogCrew</h1>
      <p>Streamlining crew scheduling for TCU sporting events</p>
      <div class="cta-buttons">
        <router-link to="/login" class="btn btn-primary">Login</router-link>
      </div>
    </header>
    
    <section class="features">
      <h2>Key Features</h2>
      <div class="feature-grid">
        <div class="feature-card">
          <h3>Automated Scheduling</h3>
          <p>Replace manual processes with efficient digital scheduling</p>
        </div>
        <div class="feature-card">
          <h3>Crew Management</h3>
          <p>Easily manage crew profiles, qualifications, and pay rates</p>
        </div>
        <div class="feature-card">
          <h3>Availability Tracking</h3>
          <p>Let crew members submit their availability for specific games</p>
        </div>
        <div class="feature-card">
          <h3>Streamlined Notifications</h3>
          <p>Centralized notifications for assignments and schedule changes</p>
        </div>
      </div>
    </section>
  </div>
</template>

<script setup>
// Home page component logic
</script>

<style scoped>
.home-container {
  max-width: 1200px;
  margin: 0 auto;
  padding: 2rem;
}

.hero {
  text-align: center;
  padding: 4rem 1rem;
  background-color: #f8f9fa;
  border-radius: 8px;
  margin-bottom: 3rem;
}

.hero h1 {
  font-size: 2.5rem;
  color: #4F2683; /* TCU Purple */
  margin-bottom: 1rem;
}

.hero p {
  font-size: 1.2rem;
  color: #555;
  margin-bottom: 2rem;
}

.cta-buttons {
  display: flex;
  justify-content: center;
  gap: 1rem;
}

.btn {
  padding: 0.75rem 1.5rem;
  border-radius: 4px;
  font-weight: bold;
  text-decoration: none;
  transition: all 0.3s ease;
}

.btn-primary {
  background-color: #4F2683; /* TCU Purple */
  color: white;
}

.btn-primary:hover {
  background-color: #3a1c60;
}

.features {
  margin-top: 3rem;
}

.features h2 {
  text-align: center;
  margin-bottom: 2rem;
  color: #333;
}

.feature-grid {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(250px, 1fr));
  gap: 2rem;
}

.feature-card {
  background-color: white;
  border-radius: 8px;
  padding: 1.5rem;
  box-shadow: 0 4px 6px rgba(0, 0, 0, 0.1);
  transition: transform 0.3s ease;
}

.feature-card:hover {
  transform: translateY(-5px);
}

.feature-card h3 {
  color: #4F2683; /* TCU Purple */
  margin-bottom: 0.5rem;
}

.feature-card p {
  color: #666;
}
</style> 