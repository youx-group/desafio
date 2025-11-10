<template>
  <div class="space-y-6">
    <!-- Header -->
    <div class="flex justify-between items-center">
      <div>
        <h1 class="text-2xl font-bold text-gray-900">
          Bem-vindo, {{ user?.nome }}!
        </h1>
        <p class="text-gray-600">
          {{ user?.role === 'MEDICO' ? 'Médico' : 'Enfermeira' }} - {{ formatDateTime(new Date().toISOString()) }}
        </p>
      </div>
      <div class="flex items-center space-x-2">
        <div class="bg-green-100 text-green-800 px-3 py-1 rounded-full text-sm font-medium">
          Sistema Online
        </div>
      </div>
    </div>

    <!-- Cards de Estatísticas -->
    <div class="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-4 gap-6">
      <RouterLink
        v-for="(card, index) in statCards"
        :key="index"
        :to="card.link"
        class="card hover:shadow-lg transition-shadow duration-200"
      >
        <div class="card-content">
          <div class="flex items-center justify-between">
            <div>
              <p class="text-sm font-medium text-gray-600">{{ card.title }}</p>
              <p class="text-3xl font-bold text-gray-900">{{ card.value }}</p>
            </div>
            <div :class="`${card.bgColor} p-3 rounded-lg`">
              <component :is="card.icon" :class="`w-6 h-6 ${card.textColor}`" />
            </div>
          </div>
        </div>
      </RouterLink>
    </div>

    <div class="grid grid-cols-1 lg:grid-cols-2 gap-6">
      <!-- Ações Rápidas -->
      <div class="card">
        <div class="card-header">
          <h2 class="text-lg font-semibold">Ações Rápidas</h2>
        </div>
        <div class="card-content">
          <div class="grid grid-cols-1 sm:grid-cols-2 gap-4">
            <RouterLink
              v-for="(action, index) in quickActions"
              :key="index"
              :to="action.link"
              class="flex items-center p-4 border rounded-lg hover:bg-gray-50 transition-colors"
            >
              <div :class="`${action.color} p-2 rounded-lg mr-3`">
                <component :is="action.icon" class="w-5 h-5 text-white" />
              </div>
              <div>
                <p class="font-medium text-gray-900">{{ action.title }}</p>
                <p class="text-sm text-gray-600">{{ action.description }}</p>
              </div>
            </RouterLink>
          </div>
        </div>
      </div>

      <!-- Consultas Recentes -->
      <div class="card">
        <div class="card-header">
          <h2 class="text-lg font-semibold">Consultas Recentes</h2>
        </div>
        <div class="card-content">
          <div v-if="recentConsultas.length === 0" class="text-gray-600 text-center py-4">
            Nenhuma consulta encontrada
          </div>
          <div v-else class="space-y-3">
            <div
              v-for="(consulta, index) in recentConsultas"
              :key="index"
              class="flex items-center justify-between p-3 bg-gray-50 rounded-lg"
            >
              <div class="flex items-center">
                <div class="bg-blue-100 p-2 rounded-full mr-3">
                  <Calendar class="w-4 h-4 text-blue-600" />
                </div>
                <div>
                  <p class="font-medium text-gray-900">
                    {{ consulta.paciente?.nome || 'Paciente não informado' }}
                  </p>
                  <p class="text-sm text-gray-600">
                    {{ formatDateTime(consulta.dataHora) }}
                  </p>
                </div>
              </div>
              <div class="flex items-center">
                <div v-if="consulta.status === 'AGENDADA'" class="flex items-center text-orange-600">
                  <Clock class="w-4 h-4 mr-1" />
                  <span class="text-sm">Agendada</span>
                </div>
                <div v-else class="flex items-center text-green-600">
                  <CheckCircle class="w-4 h-4 mr-1" />
                  <span class="text-sm">Realizada</span>
                </div>
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, computed, onMounted } from 'vue';
import { 
  Users, 
  Calendar, 
  UserPlus, 
  Activity,
  Clock,
  AlertCircle,
  CheckCircle
} from 'lucide-vue-next';
import { useAuthStore } from '@/stores/auth';
import { consultaService, pacienteService } from '@/services/api';
import { formatDateTime } from '@/utils';

const authStore = useAuthStore();
const user = computed(() => authStore.user);

interface DashboardStats {
  totalPacientes: number;
  totalConsultas: number;
  consultasHoje: number;
  consultasPendentes: number;
}

const stats = ref<DashboardStats>({
  totalPacientes: 0,
  totalConsultas: 0,
  consultasHoje: 0,
  consultasPendentes: 0,
});

const recentConsultas = ref<any[]>([]);
const isLoading = ref(true);

const statCards = computed(() => [
  {
    title: 'Total de Pacientes',
    value: stats.value.totalPacientes,
    icon: Users,
    color: 'bg-blue-500',
    textColor: 'text-blue-600',
    bgColor: 'bg-blue-50',
    link: '/pacientes'
  },
  {
    title: 'Total de Consultas',
    value: stats.value.totalConsultas,
    icon: Calendar,
    color: 'bg-green-500',
    textColor: 'text-green-600',
    bgColor: 'bg-green-50',
    link: '/consultas'
  },
  {
    title: 'Consultas Hoje',
    value: stats.value.consultasHoje,
    icon: Clock,
    color: 'bg-orange-500',
    textColor: 'text-orange-600',
    bgColor: 'bg-orange-50',
    link: '/consultas'
  },
  {
    title: 'Consultas Pendentes',
    value: stats.value.consultasPendentes,
    icon: AlertCircle,
    color: 'bg-red-500',
    textColor: 'text-red-600',
    bgColor: 'bg-red-50',
    link: '/consultas'
  },
]);

const quickActions = [
  {
    title: 'Novo Paciente',
    description: 'Cadastrar novo paciente',
    icon: UserPlus,
    color: 'bg-blue-500',
    link: '/pacientes'
  },
  {
    title: 'Nova Consulta',
    description: 'Agendar nova consulta',
    icon: Calendar,
    color: 'bg-green-500',
    link: '/consultas/nova'
  },
  {
    title: 'Ver Pacientes',
    description: 'Gerenciar pacientes',
    icon: Users,
    color: 'bg-purple-500',
    link: '/pacientes'
  },
  {
    title: 'Ver Consultas',
    description: 'Gerenciar consultas',
    icon: Activity,
    color: 'bg-indigo-500',
    link: '/consultas'
  },
];

onMounted(async () => {
  try {
    isLoading.value = true;
    
    // Carregar estatísticas
    const [pacientes, consultas] = await Promise.all([
      pacienteService.getAll(),
      consultaService.getAll(),
    ]);

    const hoje = new Date().toISOString().split('T')[0];
    const consultasHoje = consultas.filter((c: any) => 
      c.dataHora.startsWith(hoje)
    );
    const consultasPendentes = consultas.filter((c: any) => 
      c.status === 'AGENDADA' && new Date(c.dataHora) > new Date()
    );

    stats.value = {
      totalPacientes: pacientes.length,
      totalConsultas: consultas.length,
      consultasHoje: consultasHoje.length,
      consultasPendentes: consultasPendentes.length,
    };

    // Consultas recentes (últimas 5)
    const consultasRecentes = consultas
      .sort((a: any, b: any) => new Date(b.createdAt).getTime() - new Date(a.createdAt).getTime())
      .slice(0, 5);
    
    recentConsultas.value = consultasRecentes;
  } catch (error) {
    console.error('Erro ao carregar dados do dashboard:', error);
  } finally {
    isLoading.value = false;
  }
});
</script>

