import { defineStore } from 'pinia';
import { login as authLogin, logout as authLogout, getCurrentUser } from '../services/auth';

export const useUserStore = defineStore('user', {
  state: () => ({
    user: getCurrentUser(),
    loading: false,
    error: null
  }),
  
  getters: {
    isAuthenticated: (state) => !!state.user,
    isAdmin: (state) => state.user?.role === 'admin',
    isCrew: (state) => state.user?.role === 'crew',
    userName: (state) => state.user?.name || 'User'
  },
  
  actions: {
    async login(email, password) {
      this.loading = true;
      this.error = null;
      
      try {
        const userData = await authLogin(email, password);
        this.user = userData;
        return userData;
      } catch (error) {
        this.error = error.message;
        throw error;
      } finally {
        this.loading = false;
      }
    },
    
    logout() {
      authLogout();
      this.user = null;
    },
    
    clearError() {
      this.error = null;
    }
  }
}); 