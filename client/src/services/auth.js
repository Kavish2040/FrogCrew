// This is a mock authentication service
// In a real application, this would interact with your C# backend API

/**
 * Attempt to log in a user
 * @param {string} email - User's email
 * @param {string} password - User's password
 * @returns {Promise<Object>} - User data if successful
 */
export const login = async (email, password) => {
  // Simulate API call delay
  await new Promise(resolve => setTimeout(resolve, 500));
  
  // Mock authentication logic
  // In a real app, this would be an API call to your backend
  if (email === 'admin@example.com' && password === 'password') {
    const userData = {
      id: '1',
      email: 'admin@example.com',
      name: 'Admin User',
      role: 'admin'
    };
    
    // Store user data in localStorage
    localStorage.setItem('user', JSON.stringify(userData));
    
    return userData;
  } else if (email === 'crew@example.com' && password === 'password') {
    const userData = {
      id: '2',
      email: 'crew@example.com',
      name: 'Crew Member',
      role: 'crew'
    };
    
    // Store user data in localStorage
    localStorage.setItem('user', JSON.stringify(userData));
    
    return userData;
  }
  
  // If credentials are invalid
  throw new Error('Invalid email or password');
};

/**
 * Log out the current user
 */
export const logout = () => {
  localStorage.removeItem('user');
};

/**
 * Get the current authenticated user
 * @returns {Object|null} - User data or null if not authenticated
 */
export const getCurrentUser = () => {
  const userJson = localStorage.getItem('user');
  return userJson ? JSON.parse(userJson) : null;
};

/**
 * Check if a user is authenticated
 * @returns {boolean} - True if authenticated, false otherwise
 */
export const isAuthenticated = () => {
  return getCurrentUser() !== null;
};

/**
 * Check if the current user has a specific role
 * @param {string} role - Role to check
 * @returns {boolean} - True if user has the role, false otherwise
 */
export const hasRole = (role) => {
  const user = getCurrentUser();
  return user && user.role === role;
}; 

export const register = async (userData) => {
  // Simulate API delay
  await new Promise(resolve => setTimeout(resolve, 500));

  // Mock new user creation
  const newUser = {
    ...userData,
    id: Date.now().toString(),
    name: `${userData.firstName} ${userData.lastName}`
  };

  // Save to localStorage
  localStorage.setItem('user', JSON.stringify(newUser));

  return newUser;
};
