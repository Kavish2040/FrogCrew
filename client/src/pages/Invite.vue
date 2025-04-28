<template>
    <div class="page-container">
      <h1>Invite New Crew Member</h1>
  
      <form @submit.prevent="handleInvite" class="invite-form">
        <div class="form-group">
          <label for="name">Full Name</label>
          <input id="name" v-model="form.name" type="text" required />
        </div>
  
        <div class="form-group">
          <label for="email">Email</label>
          <input id="email" v-model="form.email" type="email" required />
        </div>
  
        <div class="form-group">
          <label for="role">Role</label>
          <select id="role" v-model="form.role" required>
            <option value="">Select Role</option>
            <option value="crew">Crew</option>
            <option value="admin">Admin</option>
          </select>
        </div>
  
        <button type="submit" class="btn-primary">Send Invitation</button>
      </form>
    </div>
  </template>
  
  <script setup>
  import { ref } from 'vue';
  
  const form = ref({
    name: '',
    email: '',
    role: ''
  });
  
  const handleInvite = () => {
    console.log('Inviting:', form.value);
    alert('Invitation sent successfully!');
  };
  </script>
  
  <style scoped>
  .page-container {
    max-width: 600px;
    margin: 2rem auto;
    padding: 2rem;
  }
  
  .invite-form {
    display: flex;
    flex-direction: column;
    gap: 1.5rem;
  }
  
  .form-group label {
    font-weight: bold;
  }
  
  input, select {
    padding: 0.75rem;
    border: 1px solid #ccc;
    border-radius: 1px;
  }
  </style>
  