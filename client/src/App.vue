<script setup>
import { computed } from 'vue';
import { useRoute } from 'vue-router';
import MainLayout from './layouts/MainLayout.vue';

const route = useRoute();

// Define routes that should NOT have the layout (e.g., login, register, landing page)
const layoutExcludedRoutes = ['/', '/login', '/register'];

const showLayout = computed(() => !layoutExcludedRoutes.includes(route.path));
</script>

<template>
  <component :is="showLayout ? MainLayout : 'div'">
    <router-view />
  </component>
</template>
