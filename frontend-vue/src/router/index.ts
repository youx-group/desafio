import { createRouter, createWebHistory } from 'vue-router';
import { useAuthStore } from '@/stores/auth';
import Login from '@/pages/Login.vue';
import Dashboard from '@/pages/Dashboard.vue';
import Pacientes from '@/pages/Pacientes.vue';
import Layout from '@/components/Layout.vue';

const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes: [
    {
      path: '/login',
      name: 'login',
      component: Login,
      meta: { requiresAuth: false }
    },
    {
      path: '/',
      component: Layout,
      meta: { requiresAuth: true },
      children: [
        {
          path: '',
          name: 'dashboard',
          component: Dashboard
        },
        {
          path: 'dashboard',
          redirect: '/'
        },
        {
          path: 'pacientes',
          name: 'pacientes',
          component: Pacientes
        },
        {
          path: 'consultas',
          name: 'consultas',
          component: () => import('@/pages/Consultas.vue')
        },
        {
          path: 'consultas/nova',
          name: 'nova-consulta',
          component: () => import('@/pages/NovaConsulta.vue')
        },
        {
          path: 'usuarios',
          name: 'usuarios',
          component: () => import('@/pages/Usuarios.vue'),
          meta: { requiresRole: 'MEDICO' }
        },
        {
          path: 'perfil',
          name: 'perfil',
          component: () => import('@/pages/Perfil.vue')
        }
      ]
    },
    {
      path: '/:pathMatch(.*)*',
      redirect: '/'
    }
  ]
});

// Guard de navegação para autenticação
router.beforeEach((to, from, next) => {
  const authStore = useAuthStore();
  const requiresAuth = to.matched.some(record => record.meta.requiresAuth);
  const requiresRole = to.meta.requiresRole;

  // Se a rota requer autenticação
  if (requiresAuth) {
    if (!authStore.isAuthenticated) {
      // Não está autenticado, redireciona para login
      next({ name: 'login', query: { redirect: to.fullPath } });
    } else if (requiresRole && authStore.user?.role !== requiresRole) {
      // Não tem a role necessária, redireciona para dashboard
      next({ name: 'dashboard' });
    } else {
      // Está autenticado e tem permissão
      next();
    }
  } else {
    // Rota pública
    if (to.name === 'login' && authStore.isAuthenticated) {
      // Já está autenticado, redireciona para dashboard
      next({ name: 'dashboard' });
    } else {
      next();
    }
  }
});

export default router;

