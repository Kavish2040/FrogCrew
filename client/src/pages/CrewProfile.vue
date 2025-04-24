<template>
    <div class="profile-container" v-if="profile">
      <div class="profile-card">
        <h2>{{ profile.firstName }} {{ profile.lastName }}</h2>
        <p><strong>Email:</strong> {{ profile.email }}</p>
        <p><strong>Phone:</strong> {{ profile.phone }}</p>
        <p><strong>Role:</strong> {{ profile.role }}</p>
        <p><strong>Qualified Position:</strong> {{ profile.qualifiedPosition }}</p>
      </div>
    </div>
    <div v-else class="loading-message">Loading profile...</div>
  </template>
  
  <script setup>
  import { ref, onMounted } from 'vue';
  import { useRoute } from 'vue-router';
  
  // This would typically fetch from an API. For now, mock data:
  const mockProfiles = [
    {
      id: '1',
      firstName: 'Juan',
      lastName: 'Hernandez',
      email: 'juan@example.com',
      phone: '111-111-1111',
      role: 'Crew',
      qualifiedPosition: 'Camera Operator'
    },
    {
      id: '2',
      firstName: 'Sarah',
      lastName: 'Smith',
      email: 'sarah@example.com',
      phone: '222-222-2222',
      role: 'Admin',
      qualifiedPosition: 'Director'
    },
    {
      id: '3',
      firstName: 'Mike',
      lastName: 'Evans',
      email: 'mike@example.com',
      phone: '333-333-3333',
      role: 'Admin',
      qualifiedPosition: 'Director'
    }
  ];
  
  const route = useRoute();
  const profile = ref(null);
  
  onMounted(() => {
    const crewId = route.params.id;
    profile.value = mockProfiles.find(p => p.id === crewId);
  });
  </script>
  
  <style scoped>
  .profile-container {
    display: flex;
    justify-content: center;
    align-items: center;
    padding: 2rem;
  }
  
  .profile-card {
    width: 100%;
    max-width: 500px;
    background: #fff;
    padding: 2rem;
    border-radius: 8px;
    box-shadow: 0 4px 12px rgba(0, 0, 0, 0.1);
  }
  
  h2 {
    margin-bottom: 1rem;
  }
  
  p {
    margin: 0.5rem 0;
  }
  
  .loading-message {
    text-align: center;
    padding: 3rem;
    font-size: 1.2rem;
    color: #777;
  }
  </style>
  