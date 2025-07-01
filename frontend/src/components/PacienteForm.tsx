import React, { useState, useEffect } from 'react';
import { useForm } from 'react-hook-form';
import { zodResolver } from '@hookform/resolvers/zod';
import { z } from 'zod';
import { User, UserCheck, Phone, Calendar } from 'lucide-react';
import { Paciente, PacienteRequestDTO } from '../types';
import { formatCPF, unformatCPF, isValidCPF, formatPhone, unformatPhone } from '../utils';
import { pacienteService } from '../services/api';
import toast from 'react-hot-toast';

// Schema de validação
const pacienteSchema = z.object({
  nome: z
    .string()
    .min(1, 'Nome é obrigatório')
    .min(2, 'Nome deve ter pelo menos 2 caracteres')
    .max(255, 'Nome deve ter no máximo 255 caracteres'),
  cpf: z
    .string()
    .min(1, 'CPF é obrigatório')
    .refine((val) => isValidCPF(val), 'CPF inválido'),
  telefone: z
    .string()
    .optional()
    .or(z.literal(''))
    .refine((val) => !val || val.length >= 10, 'Telefone deve ter pelo menos 10 dígitos'),
  dataDeNascimento: z
    .string()
    .min(1, 'Data de nascimento é obrigatória')
    .refine((val) => {
      const date = new Date(val);
      const today = new Date();
      return date < today && date > new Date('1900-01-01');
    }, 'Data de nascimento inválida'),
});

type PacienteFormData = z.infer<typeof pacienteSchema>;

interface PacienteFormProps {
  paciente?: Paciente | null;
  onSuccess: () => void;
  onCancel: () => void;
}

const PacienteForm: React.FC<PacienteFormProps> = ({ paciente, onSuccess, onCancel }) => {
  const [isLoading, setIsLoading] = useState(false);
  const isEditing = !!paciente;

  const {
    register,
    handleSubmit,
    setValue,
    watch,
    reset,
    formState: { errors }
  } = useForm<PacienteFormData>({
    resolver: zodResolver(pacienteSchema),
  });

  const cpfValue = watch('cpf');
  const telefoneValue = watch('telefone');

  // Preenche formulário quando está editando
  useEffect(() => {
    if (paciente) {
      reset({
        nome: paciente.nome,
        cpf: formatCPF(paciente.cpf),
        telefone: paciente.telefone ? formatPhone(paciente.telefone) : '',
        dataDeNascimento: paciente.dataDeNascimento,
      });
    }
  }, [paciente, reset]);

  // Auto-formatação do CPF
  useEffect(() => {
    if (cpfValue) {
      const formattedCPF = formatCPF(cpfValue);
      if (formattedCPF !== cpfValue) {
        setValue('cpf', formattedCPF);
      }
    }
  }, [cpfValue, setValue]);

  // Auto-formatação do telefone
  useEffect(() => {
    if (telefoneValue) {
      const formattedPhone = formatPhone(telefoneValue);
      if (formattedPhone !== telefoneValue) {
        setValue('telefone', formattedPhone);
      }
    }
  }, [telefoneValue, setValue]);

  const onSubmit = async (data: PacienteFormData) => {
    try {
      setIsLoading(true);

      const pacienteData: PacienteRequestDTO = {
        nome: data.nome.trim(),
        cpf: unformatCPF(data.cpf),
        telefone: data.telefone ? unformatPhone(data.telefone) : undefined,
        dataDeNascimento: data.dataDeNascimento,
      };

      if (isEditing && paciente) {
        await pacienteService.update(paciente.id, pacienteData);
        toast.success('Paciente atualizado com sucesso!');
      } else {
        await pacienteService.create(pacienteData);
        toast.success('Paciente cadastrado com sucesso!');
      }

      onSuccess();
    } catch (error: any) {
      console.error('Erro ao salvar paciente:', error);
      
      if (error.response?.data?.message) {
        toast.error(error.response.data.message);
      } else if (error.response?.status === 409) {
        toast.error('CPF já cadastrado para outro paciente');
      } else {
        toast.error(`Erro ao ${isEditing ? 'atualizar' : 'cadastrar'} paciente. Tente novamente.`);
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

      {/* Campo Data de Nascimento */}
      <div>
        <label htmlFor="dataDeNascimento" className="block text-sm font-medium text-gray-700 mb-1">
          Data de Nascimento *
        </label>
        <div className="relative">
          <div className="absolute inset-y-0 left-0 pl-3 flex items-center pointer-events-none">
            <Calendar className="h-5 w-5 text-gray-400" />
          </div>
          <input
            {...register('dataDeNascimento')}
            type="date"
            id="dataDeNascimento"
            max={new Date().toISOString().split('T')[0]}
            className={`input pl-10 ${errors.dataDeNascimento ? 'border-red-500 focus:ring-red-500' : ''}`}
          />
        </div>
        {errors.dataDeNascimento && (
          <p className="mt-1 text-sm text-red-600">{errors.dataDeNascimento.message}</p>
        )}
      </div>

      {/* Campo Telefone */}
      <div>
        <label htmlFor="telefone" className="block text-sm font-medium text-gray-700 mb-1">
          Telefone
        </label>
        <div className="relative">
          <div className="absolute inset-y-0 left-0 pl-3 flex items-center pointer-events-none">
            <Phone className="h-5 w-5 text-gray-400" />
          </div>
          <input
            {...register('telefone')}
            type="text"
            id="telefone"
            placeholder="(00) 00000-0000"
            maxLength={15}
            className={`input pl-10 ${errors.telefone ? 'border-red-500 focus:ring-red-500' : ''}`}
          />
        </div>
        {errors.telefone && (
          <p className="mt-1 text-sm text-red-600">{errors.telefone.message}</p>
        )}
        <p className="mt-1 text-xs text-gray-500">Campo opcional</p>
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
              {isEditing ? 'Atualizando...' : 'Cadastrando...'}
            </div>
          ) : (
            isEditing ? 'Atualizar' : 'Cadastrar'
          )}
        </button>
      </div>
    </form>
  );
};

export default PacienteForm; 