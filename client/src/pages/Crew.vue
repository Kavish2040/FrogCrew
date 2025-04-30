<template>
  <div class="crew-container">
    <h1>Crew Members</h1>

    <div class="controls">
      <label>
        Filter by Role:
        <select v-model="filterRole">
          <option value="">All</option>
          <option value="Crew">Crew</option>
          <option value="Admin">Admin</option>
        </select>
      </label>

      <label>
        Sort by:
        <select v-model="sortBy">
          <option value="name">Name</option>
          <option value="role">Role</option>
        </select>
      </label>
    </div>

    <ul class="crew-list">
      <li v-for="member in filteredAndSortedCrew" :key="member.id" class="crew-item">
        <div class="crew-info">
          <router-link :to="`/crew-profile/${member.id}`" class="crew-name">
            {{ member.name }}
          </router-link>
          <p><strong>Role:</strong> {{ member.role }}</p>
        </div>

        <button 
          v-if="userStore.isAdmin" 
          @click="handleDelete(member)" 
          class="btn-delete"
        >
          Delete
        </button>
      </li>
    </ul>

    <p v-if="filteredAndSortedCrew.length === 0">No crew members found.</p>
  </div>
</template>

<script setup>
import { ref, computed } from 'vue';
import { useUserStore } from '../stores/userStore';

const userStore = useUserStore();

const crewMembers = ref([
  { id: 1, name: 'Juan Hernandez', role: 'Crew', hasUpcomingAssignments: false },
  { id: 2, name: 'Sarah Smith', role: 'Admin', hasUpcomingAssignments: true },
  { id: 3, name: 'Mike Evans', role: 'Crew', hasUpcomingAssignments: false }
]);

const filterRole = ref('');
const sortBy = ref('name');

const filteredAndSortedCrew = computed(() => {
  return crewMembers.value
    .filter(member => !filterRole.value || member.role === filterRole.value)
    .sort((a, b) => {
      if (sortBy.value === 'name') return a.name.localeCompare(b.name);
      if (sortBy.value === 'role') return a.role.localeCompare(b.role);
      return 0;
    });
});

const handleDelete = (member) => {
  if (member.hasUpcomingAssignments) {
    alert(`⚠ Cannot delete ${member.name}. They are assigned to upcoming games.`);
    return;
  }

  if (confirm(`Are you sure you want to delete ${member.name}?`)) {
    crewMembers.value = crewMembers.value.filter(m => m.id !== member.id);
    alert(`✅ ${member.name} has been successfully deleted.`);
  }
};
</script>

<style scoped>
.crew-container {
  padding: 2rem;
  max-width: 800px;
  margin: 0 auto;
}

.controls {
  display: flex;
  gap: 1rem;
  margin-bottom: 1rem;
}

label {
  font-weight: bold;
}

select {
  margin-left: 0.5rem;
  padding: 0.3rem;
}

.crew-list {
  list-style: none;
  padding: 0;
}

.crew-item {
  background: white;
  border: 1px solid #ddd;
  padding: 1rem;
  margin-bottom: 1rem;
  border-radius: 1px;
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.crew-info {
  flex: 1;
}

.crew-name {
  font-size: 1.2rem;
  font-weight: 600;
  color: #4F2683;
  text-decoration: none;
}

.crew-name:hover {
  text-decoration: underline;
}

.btn-delete {
  background-color: #e74c3c;
  color: white;
  border: none;
  padding: 0.5rem 1rem;
  border-radius: 1px;
  cursor: pointer;
}

.btn-delete:hover {
  background-color: #c0392b;
}
</style>
