<template>
  <div class="min-h-screen bg-gradient-to-br from-blue-50 to-indigo-100 flex items-center justify-center p-4">
    <div class="max-w-md w-full bg-white rounded-lg shadow-xl p-8">
      <!-- Header -->
      <div class="text-center mb-8">
        <div class="flex justify-center mb-4">
          <div class="bg-primary-100 p-3 rounded-full">
            <Activity class="w-8 h-8 text-primary-600" />
          </div>
        </div>
        <h1 class="text-2xl font-bold text-gray-900">Sistema Clínico</h1>
        <p class="text-gray-600 mt-2">Faça login para acessar o sistema</p>
      </div>

      <!-- Formulário -->
      <form @submit="onSubmit" class="space-y-6">
        <!-- Campo CPF -->
        <div>
          <label for="cpf" class="block text-sm font-medium text-gray-700 mb-2">
            CPF
          </label>
          <div class="relative">
            <div class="absolute inset-y-0 left-0 pl-3 flex items-center pointer-events-none">
              <User class="h-5 w-5 text-gray-400" />
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

        <!-- Campo Senha -->
        <div>
          <label for="senha" class="block text-sm font-medium text-gray-700 mb-2">
            Senha
          </label>
          <div class="relative">
            <div class="absolute inset-y-0 left-0 pl-3 flex items-center pointer-events-none">
              <Lock class="h-5 w-5 text-gray-400" />
            </div>
            <input
              v-model="formData.senha"
              :type="showPassword ? 'text' : 'password'"
              id="senha"
              placeholder="Digite sua senha"
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

        <!-- Botão de Login -->
        <button
          type="submit"
          :disabled="isLoading"
          class="btn-primary btn-md w-full"
        >
          <div v-if="isLoading" class="flex items-center justify-center">
            <div class="loading-spinner w-5 h-5 mr-2"></div>
            Entrando...
          </div>
          <span v-else>Entrar</span>
        </button>
      </form>

      <!-- Divisor -->
      <div class="mt-6 mb-4">
        <div class="relative">
          <div class="absolute inset-0 flex items-center">
            <div class="w-full border-t border-gray-300" />
          </div>
          <div class="relative flex justify-center text-sm">
            <span class="px-2 bg-white text-gray-500">Ou</span>
          </div>
        </div>
      </div>

      <!-- Seção de Cadastro -->
      <div class="text-center">
        <p class="text-sm text-gray-600 mb-4">
          Não tem uma conta?
        </p>
        <button
          type="button"
          @click="showCadastroModal = true"
          class="btn-secondary btn-md w-full"
          :disabled="isLoading"
        >
          <UserPlus class="w-5 h-5 mr-2" />
          Cadastre-se
        </button>
      </div>

      <!-- Informações adicionais -->
      <div class="mt-6 text-center">
        <p class="text-sm text-gray-500">
          Sistema desenvolvido por Youx
        </p>
      </div>
    </div>

    <!-- Modal de Cadastro -->
    <Modal
      :is-open="showCadastroModal"
      @close="showCadastroModal = false"
      title="Cadastrar Novo Usuário"
      size="md"
    >
      <UsuarioForm
        @success="handleCadastroSuccess"
        @cancel="showCadastroModal = false"
      />
    </Modal>
  </div>
</template>

<script setup lang="ts">
import { ref, reactive, watch } from 'vue';
import { useRouter } from 'vue-router';
import { Eye, EyeOff, User, Lock, Activity, UserPlus } from 'lucide-vue-next';
import { useAuthStore } from '@/stores/auth';
import { formatCPF, unformatCPF, isValidCPF } from '@/utils';
import Modal from '@/components/Modal.vue';
import UsuarioForm from '@/components/UsuarioForm.vue';

const router = useRouter();
const authStore = useAuthStore();

const showPassword = ref(false);
const isLoading = ref(false);
const showCadastroModal = ref(false);

const formData = reactive({
  cpf: '',
  senha: ''
});

const errors = reactive({
  cpf: '',
  senha: ''
});

// Auto-formatação do CPF
watch(() => formData.cpf, (value) => {
  const formatted = formatCPF(value);
  if (formatted !== value) {
    formData.cpf = formatted;
  }
});

const validate = (): boolean => {
  errors.cpf = '';
  errors.senha = '';

  let isValid = true;

  if (!formData.cpf) {
    errors.cpf = 'CPF é obrigatório';
    isValid = false;
  } else if (!isValidCPF(formData.cpf)) {
    errors.cpf = 'CPF inválido';
    isValid = false;
  }

  if (!formData.senha) {
    errors.senha = 'Senha é obrigatória';
    isValid = false;
  } else if (formData.senha.length < 6) {
    errors.senha = 'Senha deve ter pelo menos 6 caracteres';
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
    const cleanCPF = unformatCPF(formData.cpf);
    await authStore.login(cleanCPF, formData.senha);
    router.push('/');
  } catch (error) {
    // O erro já é tratado no AuthStore com toast
  } finally {
    isLoading.value = false;
  }
};

const handleCadastroSuccess = () => {
  showCadastroModal.value = false;
};
</script>

