<template>
  <div class="crew-container">
    <h1>Crew Members</h1>

    <ul class="crew-list">
      <li v-for="member in crewMembers" :key="member.id" class="crew-item">
        <div class="crew-info">
          <router-link :to="`/crew-profile/${member.id}`" class="crew-name">
            {{ member.name }}
          </router-link>
          <p><strong>Role:</strong> {{ member.role }}</p>
        </div>

        <!-- Only Admins see the delete button -->
        <button 
          v-if="userStore.isAdmin" 
          @click="handleDelete(member)" 
          class="btn-delete"
        >
          Delete
        </button>
      </li>
    </ul>

    <p v-if="crewMembers.length === 0">No crew members found.</p>
  </div>
</template>

<script setup>
import { ref } from 'vue';
import { useUserStore } from '../stores/userStore';

const userStore = useUserStore();

// Example data (to be replaced with API call)
const crewMembers = ref([
  { id: 1, name: 'Juan Hernandez', role: 'Crew', hasUpcomingAssignments: false },
  { id: 2, name: 'Sarah Smith', role: 'Admin', hasUpcomingAssignments: true },
  { id: 3, name: 'Mike Evans', role: 'Crew', hasUpcomingAssignments: false }
]);

const handleDelete = (member) => {
  if (member.hasUpcomingAssignments) {
    alert(`⚠ Cannot delete ${member.name}. They are assigned to upcoming games.`);
    return;
  }

  const confirmed = confirm(`Are you sure you want to delete ${member.name}?`);
  if (confirmed) {
    // Simulate deletion
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
