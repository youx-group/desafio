import React, { useState } from 'react';
import { useForm } from 'react-hook-form';
import { zodResolver } from '@hookform/resolvers/zod';
import { z } from 'zod';
import { Eye, EyeOff, User, Lock, Mail, UserCheck } from 'lucide-react';
import { formatCPF, unformatCPF, isValidCPF } from '../utils';
import api from '../services/api';
import toast from 'react-hot-toast';

// Schema de validação
const usuarioSchema = z.object({
  nome: z
    .string()
    .min(1, 'Nome é obrigatório')
    .min(2, 'Nome deve ter pelo menos 2 caracteres')
    .max(255, 'Nome deve ter no máximo 255 caracteres'),
  cpf: z
    .string()
    .min(1, 'CPF é obrigatório')
    .refine((val) => isValidCPF(val), 'CPF inválido'),
  email: z
    .string()
    .min(1, 'Email é obrigatório')
    .email('Email inválido')
    .max(255, 'Email deve ter no máximo 255 caracteres'),
  senha: z
    .string()
    .min(1, 'Senha é obrigatória')
    .min(6, 'Senha deve ter pelo menos 6 caracteres')
    .max(255, 'Senha deve ter no máximo 255 caracteres'),
  confirmarSenha: z
    .string()
    .min(1, 'Confirmação de senha é obrigatória'),
  role: z.enum(['MEDICO', 'ENFERMEIRA'], {
    required_error: 'Tipo de usuário é obrigatório'
  }),
}).refine((data) => data.senha === data.confirmarSenha, {
  message: 'As senhas não coincidem',
  path: ['confirmarSenha'],
});

type UsuarioFormData = z.infer<typeof usuarioSchema>;

interface UsuarioFormProps {
  onSuccess: () => void;
  onCancel: () => void;
}

const UsuarioForm: React.FC<UsuarioFormProps> = ({ onSuccess, onCancel }) => {
  const [showPassword, setShowPassword] = useState(false);
  const [showConfirmPassword, setShowConfirmPassword] = useState(false);
  const [isLoading, setIsLoading] = useState(false);

  const {
    register,
    handleSubmit,
    setValue,
    watch,
    formState: { errors }
  } = useForm<UsuarioFormData>({
    resolver: zodResolver(usuarioSchema),
  });

  const cpfValue = watch('cpf');

  // Auto-formatação do CPF
  React.useEffect(() => {
    if (cpfValue) {
      const formattedCPF = formatCPF(cpfValue);
      if (formattedCPF !== cpfValue) {
        setValue('cpf', formattedCPF);
      }
    }
  }, [cpfValue, setValue]);

  const onSubmit = async (data: UsuarioFormData) => {
    try {
      setIsLoading(true);
      
      const userData = {
        nome: data.nome.trim(),
        cpf: unformatCPF(data.cpf),
        email: data.email.trim().toLowerCase(),
        senha: data.senha,
        role: data.role,
      };

      await api.post('/usuarios/registrar', userData);
      
      toast.success('Usuário cadastrado com sucesso!');
      onSuccess();
    } catch (error: any) {
      console.error('Erro ao cadastrar usuário:', error);
      
      if (error.response?.data?.message) {
        toast.error(error.response.data.message);
      } else if (error.response?.status === 409) {
        toast.error('CPF ou email já cadastrado');
      } else {
        toast.error('Erro ao cadastrar usuário. Tente novamente.');
      }
    } finally {
      setIsLoading(false);
    }
  };

  return (
    <form onSubmit={handleSubmit(onSubmit)} className="space-y-4">
      {/* Campo Nome */}
      <div>
        <label htmlFor="nome" className="block text-sm font-medium text-gray-700 mb-1">
          Nome Completo *
        </label>
        <div className="relative">
          <div className="absolute inset-y-0 left-0 pl-3 flex items-center pointer-events-none">
            <User className="h-5 w-5 text-gray-400" />
          </div>
          <input
            {...register('nome')}
            type="text"
            id="nome"
            placeholder="Digite o nome completo"
            className={`input pl-10 ${errors.nome ? 'border-red-500 focus:ring-red-500' : ''}`}
          />
        </div>
        {errors.nome && (
          <p className="mt-1 text-sm text-red-600">{errors.nome.message}</p>
        )}
      </div>

      {/* Campo CPF */}
      <div>
        <label htmlFor="cpf" className="block text-sm font-medium text-gray-700 mb-1">
          CPF *
        </label>
        <div className="relative">
          <div className="absolute inset-y-0 left-0 pl-3 flex items-center pointer-events-none">
            <UserCheck className="h-5 w-5 text-gray-400" />
          </div>
          <input
            {...register('cpf')}
            type="text"
            id="cpf"
            placeholder="000.000.000-00"
            maxLength={14}
            className={`input pl-10 ${errors.cpf ? 'border-red-500 focus:ring-red-500' : ''}`}
          />
        </div>
        {errors.cpf && (
          <p className="mt-1 text-sm text-red-600">{errors.cpf.message}</p>
        )}
      </div>

      {/* Campo Email */}
      <div>
        <label htmlFor="email" className="block text-sm font-medium text-gray-700 mb-1">
          Email *
        </label>
        <div className="relative">
          <div className="absolute inset-y-0 left-0 pl-3 flex items-center pointer-events-none">
            <Mail className="h-5 w-5 text-gray-400" />
          </div>
          <input
            {...register('email')}
            type="email"
            id="email"
            placeholder="usuario@exemplo.com"
            className={`input pl-10 ${errors.email ? 'border-red-500 focus:ring-red-500' : ''}`}
          />
        </div>
        {errors.email && (
          <p className="mt-1 text-sm text-red-600">{errors.email.message}</p>
        )}
      </div>

      {/* Campo Tipo de Usuário */}
      <div>
        <label htmlFor="role" className="block text-sm font-medium text-gray-700 mb-1">
          Tipo de Usuário *
        </label>
        <select
          {...register('role')}
          id="role"
          className={`input ${errors.role ? 'border-red-500 focus:ring-red-500' : ''}`}
        >
          <option value="">Selecione o tipo</option>
          <option value="MEDICO">Médico</option>
          <option value="ENFERMEIRA">Enfermeira</option>
        </select>
        {errors.role && (
          <p className="mt-1 text-sm text-red-600">{errors.role.message}</p>
        )}
      </div>

      {/* Campo Senha */}
      <div>
        <label htmlFor="senha" className="block text-sm font-medium text-gray-700 mb-1">
          Senha *
        </label>
        <div className="relative">
          <div className="absolute inset-y-0 left-0 pl-3 flex items-center pointer-events-none">
            <Lock className="h-5 w-5 text-gray-400" />
          </div>
          <input
            {...register('senha')}
            type={showPassword ? 'text' : 'password'}
            id="senha"
            placeholder="Digite uma senha"
            className={`input pl-10 pr-10 ${errors.senha ? 'border-red-500 focus:ring-red-500' : ''}`}
          />
          <button
            type="button"
            className="absolute inset-y-0 right-0 pr-3 flex items-center"
            onClick={() => setShowPassword(!showPassword)}
          >
            {showPassword ? (
              <EyeOff className="h-5 w-5 text-gray-400 hover:text-gray-600" />
            ) : (
              <Eye className="h-5 w-5 text-gray-400 hover:text-gray-600" />
            )}
          </button>
        </div>
        {errors.senha && (
          <p className="mt-1 text-sm text-red-600">{errors.senha.message}</p>
        )}
      </div>

      {/* Campo Confirmar Senha */}
      <div>
        <label htmlFor="confirmarSenha" className="block text-sm font-medium text-gray-700 mb-1">
          Confirmar Senha *
        </label>
        <div className="relative">
          <div className="absolute inset-y-0 left-0 pl-3 flex items-center pointer-events-none">
            <Lock className="h-5 w-5 text-gray-400" />
          </div>
          <input
            {...register('confirmarSenha')}
            type={showConfirmPassword ? 'text' : 'password'}
            id="confirmarSenha"
            placeholder="Confirme a senha"
            className={`input pl-10 pr-10 ${errors.confirmarSenha ? 'border-red-500 focus:ring-red-500' : ''}`}
          />
          <button
            type="button"
            className="absolute inset-y-0 right-0 pr-3 flex items-center"
            onClick={() => setShowConfirmPassword(!showConfirmPassword)}
          >
            {showConfirmPassword ? (
              <EyeOff className="h-5 w-5 text-gray-400 hover:text-gray-600" />
            ) : (
              <Eye className="h-5 w-5 text-gray-400 hover:text-gray-600" />
            )}
          </button>
        </div>
        {errors.confirmarSenha && (
          <p className="mt-1 text-sm text-red-600">{errors.confirmarSenha.message}</p>
        )}
      </div>

      {/* Botões */}
      <div className="flex gap-3 pt-4">
        <button
          type="button"
          onClick={onCancel}
          className="btn-secondary btn-md flex-1"
          disabled={isLoading}
        >
          Cancelar
        </button>
        <button
          type="submit"
          disabled={isLoading}
          className="btn-primary btn-md flex-1"
        >
          {isLoading ? (
            <div className="flex items-center justify-center">
              <div className="loading-spinner w-4 h-4 mr-2"></div>
              Cadastrando...
            </div>
          ) : (
            'Cadastrar'
          )}
        </button>
      </div>
    </form>
  );
};

export default UsuarioForm; 