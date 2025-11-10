<template>
  <form @submit="onSubmit" class="space-y-4">
    <!-- Campo Nome -->
    <div>
      <label for="nome" class="block text-sm font-medium text-gray-700 mb-1">
        Nome Completo *
      </label>
      <div class="relative">
        <div class="absolute inset-y-0 left-0 pl-3 flex items-center pointer-events-none">
          <User class="h-5 w-5 text-gray-400" />
        </div>
        <input
          v-model="formData.nome"
          type="text"
          id="nome"
          placeholder="Digite o nome completo"
          :class="['input pl-10', errors.nome ? 'border-red-500 focus:ring-red-500' : '']"
        />
      </div>
      <p v-if="errors.nome" class="mt-1 text-sm text-red-600">{{ errors.nome }}</p>
    </div>

    <!-- Campo CPF -->
    <div>
      <label for="cpf" class="block text-sm font-medium text-gray-700 mb-1">
        CPF *
      </label>
      <div class="relative">
        <div class="absolute inset-y-0 left-0 pl-3 flex items-center pointer-events-none">
          <UserCheck class="h-5 w-5 text-gray-400" />
        </div>
        <input
          v-model="formData.cpf"
          type="text"
          id="cpf"
          placeholder="000.000.000-00"
          maxlength="14"
          :class="['input pl-10', errors.cpf ? 'border-red-500 focus:ring-red-500' : '']"
        />
      </div>
      <p v-if="errors.cpf" class="mt-1 text-sm text-red-600">{{ errors.cpf }}</p>
    </div>

    <!-- Campo Data de Nascimento -->
    <div>
      <label for="dataDeNascimento" class="block text-sm font-medium text-gray-700 mb-1">
        Data de Nascimento *
      </label>
      <div class="relative">
        <div class="absolute inset-y-0 left-0 pl-3 flex items-center pointer-events-none">
          <Calendar class="h-5 w-5 text-gray-400" />
        </div>
        <input
          v-model="formData.dataDeNascimento"
          type="date"
          id="dataDeNascimento"
          :max="new Date().toISOString().split('T')[0]"
          :class="['input pl-10', errors.dataDeNascimento ? 'border-red-500 focus:ring-red-500' : '']"
        />
      </div>
      <p v-if="errors.dataDeNascimento" class="mt-1 text-sm text-red-600">{{ errors.dataDeNascimento }}</p>
    </div>

    <!-- Campo Telefone -->
    <div>
      <label for="telefone" class="block text-sm font-medium text-gray-700 mb-1">
        Telefone
      </label>
      <div class="relative">
        <div class="absolute inset-y-0 left-0 pl-3 flex items-center pointer-events-none">
          <Phone class="h-5 w-5 text-gray-400" />
        </div>
        <input
          v-model="formData.telefone"
          type="text"
          id="telefone"
          placeholder="(00) 00000-0000"
          maxlength="15"
          :class="['input pl-10', errors.telefone ? 'border-red-500 focus:ring-red-500' : '']"
        />
      </div>
      <p v-if="errors.telefone" class="mt-1 text-sm text-red-600">{{ errors.telefone }}</p>
      <p class="mt-1 text-xs text-gray-500">Campo opcional</p>
    </div>

    <!-- Botões -->
    <div class="flex gap-3 pt-4">
      <button
        type="button"
        @click="handleCancel"
        class="btn-secondary btn-md flex-1"
        :disabled="isLoading"
      >
        Cancelar
      </button>
      <button
        type="submit"
        :disabled="isLoading"
        class="btn-primary btn-md flex-1"
      >
        <div v-if="isLoading" class="flex items-center justify-center">
          <div class="loading-spinner w-4 h-4 mr-2"></div>
          {{ isEditing ? 'Atualizando...' : 'Cadastrando...' }}
        </div>
        <span v-else>{{ isEditing ? 'Atualizar' : 'Cadastrar' }}</span>
      </button>
    </div>
  </form>
</template>

<script setup lang="ts">
import { ref, reactive, watch, computed } from 'vue';
import { User, UserCheck, Phone, Calendar } from 'lucide-vue-next';
import type { Paciente, PacienteRequestDTO } from '@/types';
import { formatCPF, unformatCPF, isValidCPF, formatPhone, unformatPhone } from '@/utils';
import { pacienteService } from '@/services/api';
import { useToast } from 'vue-toastification';

interface Props {
  paciente?: Paciente | null;
}

interface Emits {
  (e: 'success'): void;
  (e: 'cancel'): void;
}

const props = defineProps<Props>();
const emit = defineEmits<Emits>();
const toast = useToast();

const isLoading = ref(false);
const isEditing = computed(() => !!props.paciente);

const formData = reactive({
  nome: '',
  cpf: '',
  telefone: '',
  dataDeNascimento: ''
});

const errors = reactive({
  nome: '',
  cpf: '',
  telefone: '',
  dataDeNascimento: ''
});

// Preenche formulário quando está editando
watch(() => props.paciente, (paciente) => {
  if (paciente) {
    formData.nome = paciente.nome;
    formData.cpf = formatCPF(paciente.cpf);
    formData.telefone = paciente.telefone ? formatPhone(paciente.telefone) : '';
    formData.dataDeNascimento = paciente.dataDeNascimento;
  }
}, { immediate: true });

// Auto-formatação do CPF
watch(() => formData.cpf, (value) => {
  const formatted = formatCPF(value);
  if (formatted !== value) {
    formData.cpf = formatted;
  }
});

// Auto-formatação do telefone
watch(() => formData.telefone, (value) => {
  if (value) {
    const formatted = formatPhone(value);
    if (formatted !== value) {
      formData.telefone = formatted;
    }
  }
});

const validate = (): boolean => {
  // Limpa erros
  errors.nome = '';
  errors.cpf = '';
  errors.telefone = '';
  errors.dataDeNascimento = '';

  let isValid = true;

  // Valida nome
  if (!formData.nome) {
    errors.nome = 'Nome é obrigatório';
    isValid = false;
  } else if (formData.nome.length < 2) {
    errors.nome = 'Nome deve ter pelo menos 2 caracteres';
    isValid = false;
  }

  // Valida CPF
  if (!formData.cpf) {
    errors.cpf = 'CPF é obrigatório';
    isValid = false;
  } else if (!isValidCPF(formData.cpf)) {
    errors.cpf = 'CPF inválido';
    isValid = false;
  }

  // Valida data de nascimento
  if (!formData.dataDeNascimento) {
    errors.dataDeNascimento = 'Data de nascimento é obrigatória';
    isValid = false;
  } else {
    const date = new Date(formData.dataDeNascimento);
    const today = new Date();
    if (date >= today || date < new Date('1900-01-01')) {
      errors.dataDeNascimento = 'Data de nascimento inválida';
      isValid = false;
    }
  }

  // Valida telefone (opcional)
  if (formData.telefone && unformatPhone(formData.telefone).length < 10) {
    errors.telefone = 'Telefone deve ter pelo menos 10 dígitos';
    isValid = false;
  }

  return isValid;
};

const onSubmit = async (event: Event) => {
  event.preventDefault();

  if (!validate()) {
    return;
  }

  try {
    isLoading.value = true;

    const pacienteData: PacienteRequestDTO = {
      nome: formData.nome.trim(),
      cpf: unformatCPF(formData.cpf),
      telefone: formData.telefone ? unformatPhone(formData.telefone) : undefined,
      dataDeNascimento: formData.dataDeNascimento,
    };

    if (isEditing.value && props.paciente) {
      await pacienteService.update(props.paciente.id, pacienteData);
      toast.success('Paciente atualizado com sucesso!');
    } else {
      await pacienteService.create(pacienteData);
      toast.success('Paciente cadastrado com sucesso!');
    }

    emit('success');
  } catch (error: any) {
    console.error('Erro ao salvar paciente:', error);
    
    if (error.response?.data?.message) {
      toast.error(error.response.data.message);
    } else if (error.response?.status === 409) {
      toast.error('CPF já cadastrado para outro paciente');
    } else {
      toast.error(`Erro ao ${isEditing.value ? 'atualizar' : 'cadastrar'} paciente. Tente novamente.`);
    }
  } finally {
    isLoading.value = false;
  }
};

const handleCancel = () => {
  emit('cancel');
};
</script>

