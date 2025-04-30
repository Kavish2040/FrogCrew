<template>
  <div class="page-container">
    <h1>Invite New Crew Member</h1>

    <form @submit.prevent="handleInvite" class="invite-form">
      <div class="form-group">
        <label for="name">Full Name</label>
        <input
          id="name"
          v-model="form.name"
          type="text"
          maxlength="50"
          required
        />
        <p v-if="form.name.length > 50" class="error-message">
          Name cannot exceed 50 characters.
        </p>
      </div>

      <div class="form-group">
        <label for="email">Email</label>
        <input
          id="email"
          v-model="form.email"
          type="email"
          required
        />
        <p v-if="!isValidEmail(form.email) && form.email" class="error-message">
          Please enter a valid email.
        </p>
        <p v-if="isDuplicateEmail(form.email)" class="error-message">
          This email is already in use.
        </p>
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
      <p v-if="successMessage" class="success-message">{{ successMessage }}</p>
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

const existingEmails = ['jane@example.com', 'mike@example.com']; // Simulated DB
const successMessage = ref('');

const isValidEmail = (email) => {
  const re = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
  return re.test(email);
};

const isDuplicateEmail = (email) => {
  return existingEmails.includes(email.trim().toLowerCase());
};

const handleInvite = () => {
  if (!isValidEmail(form.value.email)) return;
  if (isDuplicateEmail(form.value.email)) return;

  console.log('Inviting:', form.value);
  successMessage.value = 'Invitation sent successfully!';
  // Simulate sending email logic...
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
  display: block;
  margin-bottom: 0.5rem;
}

input,
select {
  padding: 0.75rem;
  border: 1px solid #ccc;
  border-radius: 1px;
  width: 100%;
}

.error-message {
  color: #e74c3c;
  font-size: 0.9rem;
  margin-top: 0.5rem;
}

.success-message {
  color: #2ecc71;
  margin-top: 1rem;
  font-weight: bold;
}
</style>
