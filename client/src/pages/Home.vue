<template>
  <MainLayout>
    <section class="hero-section">
      <div class="hero-content">
        <h1>Welcome to FrogCrew</h1>
        <p>Streamlining crew scheduling for TCU sporting events</p>
        <router-link to="/login" class="btn-primary">Login</router-link>
      </div>
    </section>

    <section class="features">
      <h2>Key Features</h2>
      <div class="feature-grid">
        <div class="feature-card">
          <h3>Automated Scheduling</h3>
          <p>Replace manual processes with efficient digital scheduling</p>
        </div>
        <div class="feature-card">
          <h3>Crew Management</h3>
          <p>Manage crew profiles, qualifications, and assignments with ease</p>
        </div>
        <div class="feature-card">
          <h3>Availability Tracking</h3>
          <p>Allow crew members to submit their availability for games</p>
        </div>
        <div class="feature-card">
          <h3>Streamlined Notifications</h3>
          <p>Stay updated on assignments and schedule changes</p>
        </div>
      </div>
    </section>
  </MainLayout>
</template>

<script setup>
import MainLayout from '../layouts/MainLayout.vue';
</script>

<style scoped>
.hero-section {
  height: 80vh;
  background-image: linear-gradient(
      rgba(255, 255, 255, 0.9), 
      rgba(255, 255, 255, 0.6)
    ), 
    url('@/assets/images/superfrog.jpg'); /* ← Make sure this path is correct */
  background-size: cover;
  background-position: center;
  display: flex;
  align-items: center;
  justify-content: center;
  text-align: center;
  padding: 3rem 1rem;
}

.hero-content {
  max-width: 700px;
  padding: 2rem;
}

.hero-content h1 {
  font-size: 2.8rem;
  color: var(--tcu-purple);
  font-weight: 700;
  text-shadow: 1px 1px 4px rgba(0,0,0,0.1);
  margin-bottom: 1rem;
}

.hero-content p {
  font-size: 1.2rem;
  color: var(--text-dark);
  font-weight: 400;
  line-height: 1.6;
  margin-bottom: 2rem;
}

.btn-primary {
  background-color: var(--tcu-purple);
  color: white;
  padding: 0.7rem 1.4rem;
  font-weight: 500;
  border: none;
  border-radius: 8px;
  font-size: 1rem;
  text-decoration: none;
  transition: background-color 0.3s ease;
}

.btn-primary:hover {
  background-color: var(--tcu-dark-purple);
}

.features {
  padding: 4rem 1.5rem;
  max-width: 1200px;
  margin: 0 auto;
}

.features h2 {
  text-align: center;
  margin-bottom: 2.5rem;
  color: var(--tcu-purple);
  font-size: 2rem;
  font-weight: 600;
}

.feature-grid {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(260px, 1fr));
  gap: 2rem;
}

.feature-card {
  background-color: white;
  border-radius: 10px;
  padding: 1.75rem;
  box-shadow: 0 6px 12px rgba(0, 0, 0, 0.05);
  transition: transform 0.3s ease;
  text-align: left;
}

.feature-card:hover {
  transform: translateY(-4px);
}

.feature-card h3 {
  font-size: 1.3rem;
  margin-bottom: 0.5rem;
  color: var(--tcu-purple);
}

.feature-card p {
  color: var(--text-muted);
  line-height: 1.5;
}
</style>
