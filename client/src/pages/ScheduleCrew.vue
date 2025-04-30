<template>
  <div class="page-container">
    <h1>Schedule Crew Members</h1>

    <div v-for="position in openPositions" :key="position.id" class="position-card">
      <h3>{{ position.role }} for {{ position.game }}</h3>
      <select v-model="assignments[position.id]" class="crew-dropdown">
        <option value="">Select Crew Member</option>
        <option
          v-for="member in availableCrew(position)"
          :key="member.id"
          :value="member.id"
        >
          {{ member.name }}
        </option>
      </select>
    </div>

    <div v-if="error" class="error-message">{{ error }}</div>

    <button @click="handleScheduleCrew" class="btn-primary">Save Assignments</button>
  </div>
</template>

<script setup>
import { ref } from 'vue';

// Example open positions per game
const openPositions = ref([
  { id: 1, role: 'Camera Operator', game: 'Game vs. Team A', requiredRole: 'Camera Operator' },
  { id: 2, role: 'Replay Operator', game: 'Game vs. Team B', requiredRole: 'Replay Operator' }
]);

// Simulated crew list with roles and conflicts
const crewList = ref([
  { id: 1, name: 'Sarah Smith', qualifiedRoles: ['Camera Operator'], conflictsWithGame: false },
  { id: 2, name: 'Mike Evans', qualifiedRoles: ['Replay Operator'], conflictsWithGame: true },
  { id: 3, name: 'Juan Hernandez', qualifiedRoles: ['Camera Operator', 'Replay Operator'], conflictsWithGame: false }
]);

const assignments = ref({});
const error = ref('');

const availableCrew = (position) => {
  return crewList.value.filter(member =>
    member.qualifiedRoles.includes(position.requiredRole)
  );
};

const handleScheduleCrew = () => {
  error.value = '';

  // Check for missing assignments
  const missing = openPositions.value.find(pos => !assignments.value[pos.id]);
  if (missing) {
    error.value = `⚠ Please assign someone to: ${missing.role} (${missing.game})`;
    return;
  }

  // Check for conflicts
  for (const pos of openPositions.value) {
    const memberId = assignments.value[pos.id];
    const member = crewList.value.find(m => m.id === memberId);
    if (member?.conflictsWithGame) {
      error.value = `⚠ ${member.name} has a scheduling conflict for ${pos.game}`;
      return;
    }
  }

  // Simulate saving
  console.log('✅ Crew scheduled:', assignments.value);
  alert('Crew scheduled and notified successfully!');
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
  box-shadow: var(--shadow, 0 2px 6px rgba(0,0,0,0.05));
  border-radius: 1px;
}

.crew-dropdown {
  width: 100%;
  margin-top: 1rem;
  padding: 0.75rem;
  border: 1px solid #ccc;
  border-radius: 1px;
}

.btn-primary {
  background-color: #4F2683;
  color: white;
  padding: 0.75rem 1.5rem;
  border: none;
  border-radius: 1px;
  font-weight: 600;
  cursor: pointer;
}

.btn-primary:hover {
  background-color: #3a1c60;
}

.error-message {
  color: #e74c3c;
  margin: 1rem 0;
  font-weight: 500;
}
</style>
