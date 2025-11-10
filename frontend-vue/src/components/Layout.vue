<template>
  <div class="flex h-screen bg-gray-50">
    <!-- Sidebar -->
    <div :class="[
      'fixed inset-y-0 left-0 z-50 w-64 bg-white shadow-lg transform transition-transform duration-300 ease-in-out lg:translate-x-0 lg:static lg:inset-0',
      sidebarOpen ? 'translate-x-0' : '-translate-x-full'
    ]">
      <!-- Logo -->
      <div class="flex items-center justify-center h-16 px-4 bg-blue-600">
        <div class="flex items-center">
          <Activity class="w-8 h-8 text-white" />
          <span class="ml-2 text-xl font-bold text-white">Clínica</span>
        </div>
      </div>

      <!-- Navigation -->
      <nav class="mt-5 px-2">
        <div class="space-y-1">
          <RouterLink
            v-for="item in navigation"
            :key="item.name"
            :to="item.href"
            :class="[
              item.current
                ? 'bg-blue-100 border-blue-500 text-blue-700'
                : 'border-transparent text-gray-600 hover:bg-gray-50 hover:text-gray-900',
              'group flex items-center px-2 py-2 text-sm font-medium border-l-4 transition-colors'
            ]"
            @click="sidebarOpen = false"
          >
            <component
              :is="item.icon"
              :class="[
                item.current ? 'text-blue-500' : 'text-gray-400 group-hover:text-gray-500',
                'mr-3 h-6 w-6'
              ]"
              aria-hidden="true"
            />
            {{ item.name }}
          </RouterLink>
        </div>
      </nav>

      <!-- User info at bottom -->
      <div class="absolute bottom-0 w-full p-4 border-t border-gray-200">
        <div class="flex items-center">
          <div 
            class="flex-shrink-0 w-10 h-10 rounded-full flex items-center justify-center text-white text-sm font-medium"
            :style="{ backgroundColor: stringToColor(user?.nome || '') }"
          >
            {{ getInitials(user?.nome || '') }}
          </div>
          <div class="ml-3 flex-1 min-w-0">
            <p class="text-sm font-medium text-gray-900 truncate">
              {{ user?.nome }}
            </p>
            <p class="text-xs text-gray-500">
              {{ user?.role === 'MEDICO' ? 'Médico' : 'Enfermeira' }}
            </p>
          </div>
        </div>
        
        <div class="mt-3 flex space-x-2">
          <RouterLink
            to="/perfil"
            class="flex-1 btn-outline btn-sm text-center"
            @click="sidebarOpen = false"
          >
            <User class="w-4 h-4 mr-1" />
            Perfil
          </RouterLink>
          <button
            @click="handleLogout"
            class="flex-1 btn-secondary btn-sm"
          >
            <LogOut class="w-4 h-4 mr-1" />
            Sair
          </button>
        </div>
      </div>
    </div>

    <!-- Overlay for mobile -->
    <div 
      v-if="sidebarOpen"
      class="fixed inset-0 z-40 bg-gray-600 bg-opacity-75 lg:hidden"
      @click="sidebarOpen = false"
    />

    <!-- Main content -->
    <div class="flex-1 flex flex-col overflow-hidden">
      <!-- Top bar -->
      <header class="bg-white shadow-sm border-b border-gray-200">
        <div class="flex items-center justify-between px-4 py-3">
          <div class="flex items-center">
            <button
              type="button"
              class="text-gray-500 hover:text-gray-700 lg:hidden"
              @click="sidebarOpen = true"
            >
              <Menu class="w-6 h-6" />
            </button>
            
            <div class="ml-4 lg:ml-0">
              <h1 class="text-lg font-semibold text-gray-900">
                Sistema Clínico
              </h1>
            </div>
          </div>

          <div class="flex items-center space-x-3">
            <!-- Notifications -->
            <button class="p-2 text-gray-400 hover:text-gray-500 relative">
              <Bell class="w-6 h-6" />
              <span class="absolute top-0 right-0 block h-3 w-3 rounded-full bg-red-400 ring-2 ring-white" />
            </button>

            <!-- Settings -->
            <button class="p-2 text-gray-400 hover:text-gray-500">
              <Settings class="w-6 h-6" />
            </button>

            <!-- User avatar -->
            <div class="flex items-center">
              <div 
                class="w-8 h-8 rounded-full flex items-center justify-center text-white text-sm font-medium"
                :style="{ backgroundColor: stringToColor(user?.nome || '') }"
              >
                {{ getInitials(user?.nome || '') }}
              </div>
            </div>
          </div>
        </div>
      </header>

      <!-- Page content -->
      <main class="flex-1 overflow-y-auto">
        <div class="p-6">
          <RouterView />
        </div>
      </main>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, computed } from 'vue';
import { useRoute } from 'vue-router';
import { 
  Menu, 
  Home, 
  Users, 
  Calendar, 
  UserCog, 
  User, 
  LogOut,
  Activity,
  Bell,
  Settings
} from 'lucide-vue-next';
import { useAuthStore } from '@/stores/auth';
import { getInitials, stringToColor } from '@/utils';

const route = useRoute();
const authStore = useAuthStore();
const sidebarOpen = ref(false);

const user = computed(() => authStore.user);

const navigation = computed(() => [
  {
    name: 'Dashboard',
    href: '/',
    icon: Home,
    current: route.path === '/' || route.path === '/dashboard'
  },
  {
    name: 'Pacientes',
    href: '/pacientes',
    icon: Users,
    current: route.path.startsWith('/pacientes')
  },
  {
    name: 'Consultas',
    href: '/consultas',
    icon: Calendar,
    current: route.path.startsWith('/consultas')
  },
  ...(user.value?.role === 'MEDICO' ? [{
    name: 'Usuários',
    href: '/usuarios',
    icon: UserCog,
    current: route.path.startsWith('/usuarios')
  }] : []),
]);

const handleLogout = () => {
  authStore.logout();
};
</script>

