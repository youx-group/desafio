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

    <!-- Campo Tipo de Usuário -->
    <div>
      <label for="role" class="block text-sm font-medium text-gray-700 mb-1">
        Tipo de Usuário *
      </label>
      <select
        v-model="formData.role"
        id="role"
        :class="['input', errors.role ? 'border-red-500 focus:ring-red-500' : '']"
      >
        <option value="">Selecione o tipo</option>
        <option value="MEDICO">Médico</option>
        <option value="ENFERMEIRA">Enfermeira</option>
      </select>
      <p v-if="errors.role" class="mt-1 text-sm text-red-600">{{ errors.role }}</p>
    </div>

    <!-- Campo Senha -->
    <div>
      <label for="senha" class="block text-sm font-medium text-gray-700 mb-1">
        Senha *
      </label>
      <div class="relative">
        <div class="absolute inset-y-0 left-0 pl-3 flex items-center pointer-events-none">
          <Lock class="h-5 w-5 text-gray-400" />
        </div>
        <input
          v-model="formData.senha"
          :type="showPassword ? 'text' : 'password'"
          id="senha"
          placeholder="Digite uma senha"
          :class="['input pl-10 pr-10', errors.senha ? 'border-red-500 focus:ring-red-500' : '']"
        />
        <button
          type="button"
          class="absolute inset-y-0 right-0 pr-3 flex items-center"
          @click="showPassword = !showPassword"
        >
          <EyeOff v-if="showPassword" class="h-5 w-5 text-gray-400 hover:text-gray-600" />
          <Eye v-else class="h-5 w-5 text-gray-400 hover:text-gray-600" />
        </button>
      </div>
      <p v-if="errors.senha" class="mt-1 text-sm text-red-600">{{ errors.senha }}</p>
    </div>

    <!-- Campo Confirmar Senha -->
    <div>
      <label for="confirmarSenha" class="block text-sm font-medium text-gray-700 mb-1">
        Confirmar Senha *
      </label>
      <div class="relative">
        <div class="absolute inset-y-0 left-0 pl-3 flex items-center pointer-events-none">
          <Lock class="h-5 w-5 text-gray-400" />
        </div>
        <input
          v-model="formData.confirmarSenha"
          :type="showConfirmPassword ? 'text' : 'password'"
          id="confirmarSenha"
          placeholder="Confirme a senha"
          :class="['input pl-10 pr-10', errors.confirmarSenha ? 'border-red-500 focus:ring-red-500' : '']"
        />
        <button
          type="button"
          class="absolute inset-y-0 right-0 pr-3 flex items-center"
          @click="showConfirmPassword = !showConfirmPassword"
        >
          <EyeOff v-if="showConfirmPassword" class="h-5 w-5 text-gray-400 hover:text-gray-600" />
          <Eye v-else class="h-5 w-5 text-gray-400 hover:text-gray-600" />
        </button>
      </div>
      <p v-if="errors.confirmarSenha" class="mt-1 text-sm text-red-600">{{ errors.confirmarSenha }}</p>
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
          Cadastrando...
        </div>
        <span v-else>Cadastrar</span>
      </button>
    </div>
  </form>
</template>

<script setup lang="ts">
import { ref, reactive, watch } from 'vue';
import { Eye, EyeOff, User, Lock, UserCheck } from 'lucide-vue-next';
import { formatCPF, unformatCPF, isValidCPF } from '@/utils';
import { usuarioService } from '@/services/api';
import { useToast } from 'vue-toastification';

interface Emits {
  (e: 'success'): void;
  (e: 'cancel'): void;
}

const emit = defineEmits<Emits>();
const toast = useToast();

const showPassword = ref(false);
const showConfirmPassword = ref(false);
const isLoading = ref(false);

const formData = reactive({
  nome: '',
  cpf: '',
  senha: '',
  confirmarSenha: '',
  role: ''
});

const errors = reactive({
  nome: '',
  cpf: '',
  senha: '',
  confirmarSenha: '',
  role: ''
});

// Auto-formatação do CPF
watch(() => formData.cpf, (value) => {
  const formatted = formatCPF(value);
  if (formatted !== value) {
    formData.cpf = formatted;
  }
});

const validate = (): boolean => {
  // Limpa erros
  errors.nome = '';
  errors.cpf = '';
  errors.senha = '';
  errors.confirmarSenha = '';
  errors.role = '';

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

  // Valida role
  if (!formData.role) {
    errors.role = 'Tipo de usuário é obrigatório';
    isValid = false;
  }

  // Valida senha
  if (!formData.senha) {
    errors.senha = 'Senha é obrigatória';
    isValid = false;
  } else if (formData.senha.length < 6) {
    errors.senha = 'Senha deve ter pelo menos 6 caracteres';
    isValid = false;
  }

  // Valida confirmar senha
  if (!formData.confirmarSenha) {
    errors.confirmarSenha = 'Confirmação de senha é obrigatória';
    isValid = false;
  } else if (formData.senha !== formData.confirmarSenha) {
    errors.confirmarSenha = 'As senhas não coincidem';
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
    
    const userData = {
      nome: formData.nome.trim(),
      cpf: unformatCPF(formData.cpf),
      senha: formData.senha,
      role: formData.role,
    };

    await usuarioService.create(userData);
    
    toast.success('Usuário cadastrado com sucesso!');
    emit('success');
  } catch (error: any) {
    console.error('Erro ao cadastrar usuário:', error);
    
    if (error.response?.data?.message) {
      toast.error(error.response.data.message);
    } else if (error.response?.status === 409) {
      toast.error('CPF já cadastrado');
    } else {
      toast.error('Erro ao cadastrar usuário. Tente novamente.');
    }
  } finally {
    isLoading.value = false;
  }
};

const handleCancel = () => {
  emit('cancel');
};
</script>

