<template>
  <RouterView />
</template>

<script setup lang="ts">
import { onMounted } from 'vue';
import { useAuthStore } from '@/stores/auth';

const authStore = useAuthStore();

onMounted(() => {
  // Valida o token ao montar a aplicação
  authStore.validateToken();
});
</script>

<style>
/* Estilos globais já estão em globals.css */
</style>

