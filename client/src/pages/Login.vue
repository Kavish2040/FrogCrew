<template>
  <div class="login-container">
    <div class="login-card">
      <div class="login-header">
        <h1>FrogCrew Login</h1>
        <p>Sign in to access your account</p>
      </div>
      
      <form @submit.prevent="handleLogin" class="login-form">
        <div class="form-group">
          <label for="email">Email</label>
          <input 
            type="email" 
            id="email" 
            v-model="email" 
            placeholder="Enter your email"
            required
          />
        </div>
        
        <div class="form-group">
          <label for="password">Password</label>
          <input 
            type="password" 
            id="password" 
            v-model="password" 
            placeholder="Enter your password"
            required
          />
        </div>
        
        <div v-if="userStore.error" class="error-message">
          {{ userStore.error }}
        </div>
        
        <button type="submit" class="btn-login" :disabled="userStore.loading">
          {{ userStore.loading ? 'Signing in...' : 'Sign In' }}
        </button>
      </form>
      
      <div class="login-footer">
        <p>Don't have an account? Contact your administrator for an invitation.</p>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref } from 'vue';
import { useRouter } from 'vue-router';
import { useUserStore } from '../stores/userStore';

const router = useRouter();
const userStore = useUserStore();
const email = ref('');
const password = ref('');

const handleLogin = async () => {
  try {
    await userStore.login(email.value, password.value);
    router.push('/dashboard');
  } catch (error) {
    // Error is already handled in the store
    console.error('Login failed:', error);
  }
};
</script>

<style scoped>
.login-container {
  display: flex;
  justify-content: center;
  align-items: center;
  min-height: 100vh;
  background-color: #f5f5f5;
  padding: 1rem;
}

.login-card {
  width: 100%;
  max-width: 400px;
  background-color: white;
  border-radius: 8px;
  box-shadow: 0 4px 10px rgba(0, 0, 0, 0.1);
  overflow: hidden;
}

.login-header {
  background-color: #4F2683; /* TCU Purple */
  color: white;
  padding: 1.5rem;
  text-align: center;
}

.login-header h1 {
  margin: 0;
  font-size: 1.8rem;
}

.login-header p {
  margin: 0.5rem 0 0;
  opacity: 0.9;
}

.login-form {
  padding: 2rem;
}

.form-group {
  margin-bottom: 1.5rem;
}

.form-group label {
  display: block;
  margin-bottom: 0.5rem;
  font-weight: 500;
  color: #333;
}

.form-group input {
  width: 100%;
  padding: 0.75rem;
  border: 1px solid #ddd;
  border-radius: 4px;
  font-size: 1rem;
}

.error-message {
  color: #e74c3c;
  margin-bottom: 1rem;
  font-size: 0.9rem;
}

.btn-login {
  width: 100%;
  padding: 0.75rem;
  background-color: #4F2683; /* TCU Purple */
  color: white;
  border: none;
  border-radius: 4px;
  font-size: 1rem;
  font-weight: 500;
  cursor: pointer;
  transition: background-color 0.3s;
}

.btn-login:hover {
  background-color: #3a1c60;
}

.btn-login:disabled {
  background-color: #8e7baa;
  cursor: not-allowed;
}

.login-footer {
  padding: 1rem 2rem;
  text-align: center;
  border-top: 1px solid #eee;
  color: #666;
  font-size: 0.9rem;
}
</style> 