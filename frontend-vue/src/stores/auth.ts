import { defineStore } from 'pinia';
import { ref, computed } from 'vue';
import type { Usuario } from '@/types';
import { authService } from '@/services/api';
import { useToast } from 'vue-toastification';

export const useAuthStore = defineStore('auth', () => {
  const toast = useToast();
  
  // State
  const token = ref<string | null>(localStorage.getItem('token'));
  
  // Inicializa o usuário a partir do localStorage
  const savedUser = localStorage.getItem('user');
  const user = ref<Usuario | null>(savedUser ? JSON.parse(savedUser) : null);
  
  const isLoading = ref(false);

  // Getters
  const isAuthenticated = computed(() => !!token.value && !!user.value);

  // Actions
  const login = async (cpf: string, senha: string) => {
    try {
      isLoading.value = true;
      const response = await authService.login({ cpf, senha });
      
      token.value = response.token;
      user.value = response.usuario;
      
      localStorage.setItem('token', response.token);
      localStorage.setItem('user', JSON.stringify(response.usuario));
      
      toast.success('Login realizado com sucesso!');
    } catch (error: any) {
      toast.error(error.message || 'Erro ao fazer login');
      throw error;
    } finally {
      isLoading.value = false;
    }
  };

  const logout = () => {
    token.value = null;
    user.value = null;
    localStorage.removeItem('token');
    localStorage.removeItem('user');
    toast.success('Logout realizado com sucesso!');
  };

  const refreshToken = async () => {
    try {
      const response = await authService.refreshToken();
      token.value = response.token;
      user.value = response.usuario;
      localStorage.setItem('token', response.token);
      localStorage.setItem('user', JSON.stringify(response.usuario));
    } catch (error) {
      logout();
      throw error;
    }
  };

  const validateToken = async () => {
    if (token.value) {
      try {
        await authService.validateToken();
      } catch (error) {
        logout();
      }
    }
  };

  return {
    token,
    user,
    isLoading,
    isAuthenticated,
    login,
    logout,
    refreshToken,
    validateToken
  };
});

