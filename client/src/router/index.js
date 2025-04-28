import { createRouter, createWebHistory } from 'vue-router';

// Import pages
import Home from '../pages/Home.vue';
import CrewProfile from '../pages/CrewProfile.vue';
import Login from '../pages/Login.vue';
import Register from '../pages/Register.vue';
import Dashboard from '../pages/Dashboard.vue';
import NotFound from '../pages/NotFound.vue';
import Crew from '../pages/Crew.vue';
import GameSchedule from '../pages/GameSchedule.vue';
import CrewList from '../pages/CrewList.vue';
import Availability from '../pages/Availability.vue';
import Invite from '../pages/Invite.vue';
import CreateSchedule from '../pages/CreateSchedule.vue';
import AddGame from '../pages/AddGame.vue';
import ScheduleCrew from '../pages/ScheduleCrew.vue';


// Define routes
const routes = [  
  {
    path: '/',
    name: 'Home',
    component: Home,
    meta: { requiresAuth: false }
  },
  {
    path: '/login',
    name: 'Login',
    component: Login,
    meta: { requiresAuth: false }
  },
  {
    path: '/crew',
    name: 'Crew',
    component: Crew,
    meta: { requiresAuth: true }
  },
  { path: '/games', 
    name: 'GameSchedule', 
    component: GameSchedule, 
    meta: { requiresAuth: true } 
  },
  { path: '/crewlist', 
    name: 'CrewList', 
    component: CrewList, 
    meta: { requiresAuth: true } 
  },
  {
    path: '/crewlist/:gameId',
    name: 'CrewList',
    component: () => import('../pages/CrewList.vue'),
    meta: { requiresAuth: true }
  },  
  {
    path: '/crew-profile/:id',
    name: 'CrewProfile',
    component: () => import('../pages/CrewProfile.vue'),
    meta: { requiresAuth: true }
  },
  {
    path: '/register',
    name: 'Register',
    component: Register,
    meta: { requiresAuth: false }
  },
  {
    path: '/dashboard',
    name: 'Dashboard',
    component: Dashboard,
    meta: { requiresAuth: true }
  },
  {
    path: '/availability',
    name: 'Availability',
    component: Availability,
    meta: { requiresAuth: true }
  },
  {
    path: '/invite',
    name: 'Invite',
    component: Invite,
    meta: { requiresAuth: true }
  },
  {
    path: '/createschedule',
    name: 'CreateSchedule',
    component: CreateSchedule,
    meta: { requiresAuth: true }
  },
  {
    path: '/addgame',
    name: 'AddGame',
    component: AddGame,
    meta: { requiresAuth: true }
  },
  {
    path: '/schedulecrew',
    name: 'ScheduleCrew',
    component: ScheduleCrew,
    meta: { requiresAuth: true }
  },  
  {
    path: '/:pathMatch(.*)*',
    name: 'NotFound',
    component: NotFound,
    meta: { requiresAuth: false }
  }
];

// Create router instance
const router = createRouter({
  history: createWebHistory(),
  routes
});

// Navigation guard for authentication
router.beforeEach((to, from, next) => {
  const isAuthenticated = localStorage.getItem('user') !== null;
  
  if (to.meta.requiresAuth && !isAuthenticated) {
    next({ name: 'Login' });
  } else {
    next();
  }
});

export default router; 