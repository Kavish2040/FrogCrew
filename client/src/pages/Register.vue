<template>
    <div class="register-container">
      <div class="register-card">
        <h1>Create Your Crew Member Profile</h1>
  
        <form @submit.prevent="handleRegister" class="register-form">
          <!-- Dynamic form fields -->
          <div class="form-group" v-for="(field, key) in fields" :key="key">
            <label :for="key">{{ field.label }}</label>
            <input
              :type="field.type"
              :id="key"
              v-model="form[key]"
              :placeholder="field.placeholder"
              :pattern="field.pattern"
              :required="field.required"
            />
          </div>
  
          <!-- Role Selection -->
          <div class="form-group">
            <label for="role">Role</label>
            <select id="role" v-model="form.role" required>
              <option value="">Select a role</option>
              <option value="crew">Crew</option>
              <option value="admin">Admin</option>
            </select>
          </div>
  
          <!-- Qualified Position -->
          <div class="form-group">
            <label for="qualifiedPosition">Qualified Position</label>
            <input
              type="text"
              id="qualifiedPosition"
              v-model="form.qualifiedPosition"
              placeholder="e.g. Camera Operator"
              required
            />
          </div>
  
          <!-- Error message -->
          <div v-if="error" class="error-message">{{ error }}</div>
  
          <button type="submit" class="btn-register">Register</button>
  
          <p class="login-redirect">
            Already have an account?
            <router-link to="/login">Sign In</router-link>
          </p>
        </form>
      </div>
    </div>
  </template>
  
  <script setup>
  import { ref } from 'vue';
  import { useRouter } from 'vue-router';
  import { register } from '../services/auth';
  
  const router = useRouter();
  const error = ref(null);
  
  const form = ref({
    firstName: '',
    lastName: '',
    email: '',
    phone: '',
    password: '',
    role: '',
    qualifiedPosition: ''
  });
  
  const fields = {
    firstName: {
      label: 'First Name',
      type: 'text',
      placeholder: 'Enter first name',
      required: true
    },
    lastName: {
      label: 'Last Name',
      type: 'text',
      placeholder: 'Enter last name',
      required: true
    },
    email: {
      label: 'Email',
      type: 'email',
      placeholder: 'Enter email',
      required: true
    },
    phone: {
      label: 'Phone Number',
      type: 'tel',
      placeholder: '999-999-9999',
      required: true,
      pattern: '[0-9]{3}-[0-9]{3}-[0-9]{4}'
    },
    password: {
      label: 'Password',
      type: 'password',
      placeholder: 'Create a password',
      required: true
    }
  };
  
  const handleRegister = async () => {
    error.value = null;
    try {
      await register(form.value);
      router.push('/login');
    } catch (err) {
      error.value = err.message || 'Registration failed.';
    }
  };
  </script>
  
  <style scoped>
  .register-container {
    display: flex;
    justify-content: center;
    align-items: center;
    min-height: 100vh;
    background-color: #f5f5f5;
    padding: 2rem;
  }
  
  .register-card {
    width: 100%;
    max-width: 500px;
    background: white;
    padding: 2rem;
    border-radius: 8px;
    box-shadow: 0 4px 12px rgba(0, 0, 0, 0.1);
  }
  
  h1 {
    margin-bottom: 1.5rem;
    font-size: 1.5rem;
    color: #4F2683;
  }
  
  .form-group {
    margin-bottom: 1.25rem;
  }
  
  label {
    display: block;
    margin-bottom: 0.5rem;
    font-weight: bold;
  }
  
  input,
  select {
    width: 100%;
    padding: 0.75rem;
    border: 1px solid #ccc;
    border-radius: 4px;
  }
  
  .error-message {
    color: #e74c3c;
    margin-bottom: 1rem;
  }
  
  .btn-register {
    width: 100%;
    background-color: #4F2683;
    color: white;
    padding: 0.75rem;
    border: none;
    border-radius: 4px;
    font-weight: bold;
    cursor: pointer;
  }
  
  .btn-register:hover {
    background-color: #3a1c60;
  }
  
  .login-redirect {
    text-align: center;
    margin-top: 1rem;
  }
  </style>
  