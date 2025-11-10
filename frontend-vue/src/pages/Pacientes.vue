<template>
  <div class="space-y-6">
    <!-- Header -->
    <div class="flex justify-between items-center">
      <div>
        <h1 class="text-2xl font-bold text-gray-900">Pacientes</h1>
        <p class="text-gray-600">Gerencie os pacientes da clínica</p>
      </div>
      <button
        @click="handleNovoPaciente"
        class="btn-primary btn-md"
      >
        <Plus class="w-5 h-5 mr-2" />
        Novo Paciente
      </button>
    </div>

    <!-- Barra de pesquisa -->
    <div class="card">
      <div class="card-content">
        <div class="relative">
          <Search class="absolute left-3 top-1/2 transform -translate-y-1/2 text-gray-400 w-5 h-5" />
          <input
            v-model="searchTerm"
            type="text"
            placeholder="Buscar por nome, CPF ou telefone..."
            class="input pl-10 w-full"
          />
        </div>
      </div>
    </div>

    <!-- Estatísticas -->
    <div class="grid grid-cols-1 md:grid-cols-3 gap-6">
      <div class="card">
        <div class="card-content">
          <div class="flex items-center">
            <div class="bg-blue-100 p-3 rounded-full">
              <User class="w-6 h-6 text-blue-600" />
            </div>
            <div class="ml-4">
              <p class="text-sm font-medium text-gray-600">Total de Pacientes</p>
              <p class="text-2xl font-bold text-gray-900">{{ pacientes.length }}</p>
            </div>
          </div>
        </div>
      </div>

      <div class="card">
        <div class="card-content">
          <div class="flex items-center">
            <div class="bg-green-100 p-3 rounded-full">
              <UserCheck class="w-6 h-6 text-green-600" />
            </div>
            <div class="ml-4">
              <p class="text-sm font-medium text-gray-600">Pacientes Ativos</p>
              <p class="text-2xl font-bold text-gray-900">{{ filteredPacientes.length }}</p>
            </div>
          </div>
        </div>
      </div>

      <div class="card">
        <div class="card-content">
          <div class="flex items-center">
            <div class="bg-purple-100 p-3 rounded-full">
              <Calendar class="w-6 h-6 text-purple-600" />
            </div>
            <div class="ml-4">
              <p class="text-sm font-medium text-gray-600">Cadastros Hoje</p>
              <p class="text-2xl font-bold text-gray-900">
                {{ pacientes.filter(p => 
                  new Date(p.createdAt).toDateString() === new Date().toDateString()
                ).length }}
              </p>
            </div>
          </div>
        </div>
      </div>
    </div>

    <!-- Lista de pacientes -->
    <div class="card">
      <div class="card-header">
        <h2 class="text-lg font-semibold">
          Lista de Pacientes ({{ filteredPacientes.length }})
        </h2>
      </div>
      <div class="card-content p-0">
        <div v-if="isLoading" class="flex items-center justify-center min-h-96">
          <div class="loading-spinner w-8 h-8"></div>
        </div>
        <div v-else-if="filteredPacientes.length === 0" class="text-center py-12">
          <User class="w-12 h-12 text-gray-400 mx-auto mb-4" />
          <h3 class="text-lg font-medium text-gray-900 mb-2">
            {{ searchTerm ? 'Nenhum paciente encontrado' : 'Nenhum paciente cadastrado' }}
          </h3>
          <p class="text-gray-500 mb-4">
            {{ searchTerm 
              ? 'Tente buscar com outros termos'
              : 'Comece cadastrando o primeiro paciente da clínica'
            }}
          </p>
          <button
            v-if="!searchTerm"
            @click="handleNovoPaciente"
            class="btn-primary btn-md"
          >
            <Plus class="w-5 h-5 mr-2" />
            Cadastrar Primeiro Paciente
          </button>
        </div>
        <div v-else class="overflow-x-auto">
          <table class="table">
            <thead class="table-header">
              <tr>
                <th class="table-head">Nome</th>
                <th class="table-head">CPF</th>
                <th class="table-head">Idade</th>
                <th class="table-head">Telefone</th>
                <th class="table-head">Cadastro</th>
                <th class="table-head">Ações</th>
              </tr>
            </thead>
            <tbody class="table-body">
              <tr v-for="paciente in filteredPacientes" :key="paciente.id" class="table-row">
                <td class="table-cell">
                  <div class="flex items-center">
                    <div class="avatar">
                      <div class="avatar-fallback">
                        {{ paciente.nome.charAt(0).toUpperCase() }}
                      </div>
                    </div>
                    <div class="ml-4">
                      <div class="text-sm font-medium text-gray-900">
                        {{ paciente.nome }}
                      </div>
                    </div>
                  </div>
                </td>
                <td class="table-cell">
                  <span class="text-sm text-gray-900 font-mono">
                    {{ formatCPF(paciente.cpf) }}
                  </span>
                </td>
                <td class="table-cell">
                  <span class="text-sm text-gray-900">
                    {{ calculateAge(paciente.dataDeNascimento) }} anos
                  </span>
                </td>
                <td class="table-cell">
                  <div v-if="paciente.telefone" class="flex items-center text-sm text-gray-900">
                    <Phone class="w-4 h-4 mr-1 text-gray-400" />
                    {{ formatPhone(paciente.telefone) }}
                  </div>
                  <span v-else class="text-sm text-gray-500">-</span>
                </td>
                <td class="table-cell">
                  <span class="text-sm text-gray-500">
                    {{ formatDate(paciente.createdAt) }}
                  </span>
                </td>
                <td class="table-cell">
                  <div class="flex items-center space-x-2">
                    <button
                      @click="handleEditarPaciente(paciente)"
                      class="btn-ghost btn-sm"
                      title="Editar paciente"
                    >
                      <Edit class="w-4 h-4" />
                    </button>
                    <button
                      @click="handleExcluirPaciente(paciente)"
                      class="btn-ghost btn-sm text-red-600 hover:text-red-700 hover:bg-red-50"
                      title="Excluir paciente"
                    >
                      <Trash2 class="w-4 h-4" />
                    </button>
                  </div>
                </td>
              </tr>
            </tbody>
          </table>
        </div>
      </div>
    </div>

    <!-- Modal de cadastro/edição -->
    <Modal
      :is-open="showModal"
      @close="handleModalCancel"
      :title="editingPaciente ? 'Editar Paciente' : 'Novo Paciente'"
      size="md"
    >
      <PacienteForm
        :paciente="editingPaciente"
        @success="handleModalSuccess"
        @cancel="handleModalCancel"
      />
    </Modal>

    <!-- Modal de confirmação de exclusão -->
    <Modal
      :is-open="showDeleteModal"
      @close="showDeleteModal = false"
      title="Confirmar Exclusão"
      size="sm"
    >
      <div class="space-y-4">
        <p class="text-gray-600">
          Tem certeza que deseja excluir o paciente <strong>{{ deletingPaciente?.nome }}</strong>?
        </p>
        <p class="text-sm text-red-600">
          Esta ação não pode ser desfeita.
        </p>
        <div class="flex gap-3 pt-4">
          <button
            @click="showDeleteModal = false"
            class="btn-secondary btn-md flex-1"
          >
            Cancelar
          </button>
          <button
            @click="confirmarExclusao"
            class="btn-destructive btn-md flex-1"
          >
            Excluir
          </button>
        </div>
      </div>
    </Modal>
  </div>
</template>

<script setup lang="ts">
import { ref, computed, onMounted, watch } from 'vue';
import { Plus, Search, Edit, Trash2, Calendar, Phone, User, UserCheck } from 'lucide-vue-next';
import type { Paciente } from '@/types';
import { pacienteService } from '@/services/api';
import { formatCPF, formatPhone, formatDate, calculateAge } from '@/utils';
import { useToast } from 'vue-toastification';
import Modal from '@/components/Modal.vue';
import PacienteForm from '@/components/PacienteForm.vue';

const toast = useToast();

const pacientes = ref<Paciente[]>([]);
const searchTerm = ref('');
const isLoading = ref(true);
const showModal = ref(false);
const showDeleteModal = ref(false);
const editingPaciente = ref<Paciente | null>(null);
const deletingPaciente = ref<Paciente | null>(null);

// Filtra pacientes com base no termo de busca
const filteredPacientes = computed(() => {
  if (!searchTerm.value) {
    return pacientes.value;
  }
  
  const term = searchTerm.value.toLowerCase();
  return pacientes.value.filter(paciente =>
    paciente.nome.toLowerCase().includes(term) ||
    paciente.cpf.includes(term) ||
    paciente.telefone?.includes(term)
  );
});

const carregarPacientes = async () => {
  try {
    isLoading.value = true;
    const data = await pacienteService.getAll();
    pacientes.value = data;
  } catch (error) {
    console.error('Erro ao carregar pacientes:', error);
    toast.error('Erro ao carregar pacientes');
  } finally {
    isLoading.value = false;
  }
};

const handleNovoPaciente = () => {
  editingPaciente.value = null;
  showModal.value = true;
};

const handleEditarPaciente = (paciente: Paciente) => {
  editingPaciente.value = paciente;
  showModal.value = true;
};

const handleExcluirPaciente = (paciente: Paciente) => {
  deletingPaciente.value = paciente;
  showDeleteModal.value = true;
};

const confirmarExclusao = async () => {
  if (!deletingPaciente.value) return;

  try {
    await pacienteService.delete(deletingPaciente.value.id);
    toast.success('Paciente excluído com sucesso!');
    showDeleteModal.value = false;
    deletingPaciente.value = null;
    carregarPacientes();
  } catch (error) {
    console.error('Erro ao excluir paciente:', error);
    toast.error('Erro ao excluir paciente');
  }
};

const handleModalSuccess = () => {
  showModal.value = false;
  editingPaciente.value = null;
  carregarPacientes();
};

const handleModalCancel = () => {
  showModal.value = false;
  editingPaciente.value = null;
};

onMounted(() => {
  carregarPacientes();
});
</script>

