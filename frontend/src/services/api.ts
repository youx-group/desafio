import axios, { AxiosError, AxiosResponse } from 'axios';
import { LoginRequestDTO, LoginResponseDTO, ApiError } from '../types';

// Configuração base da API
const API_URL = import.meta.env.VITE_API_URL || 'http://localhost:8080/api/v1';

const api = axios.create({
  baseURL: API_URL,
  headers: {
    'Content-Type': 'application/json',
  },
});

// Interceptor para adicionar token JWT automaticamente
api.interceptors.request.use(
  (config) => {
    const token = localStorage.getItem('token');
    if (token) {
      config.headers.Authorization = `Bearer ${token}`;
    }
    return config;
  },
  (error) => {
    return Promise.reject(error);
  }
);

// Interceptor para tratamento de respostas
api.interceptors.response.use(
  (response: AxiosResponse) => {
    return response;
  },
  async (error: AxiosError) => {
    if (error.response?.status === 401) {
      // Token expirado ou inválido
      localStorage.removeItem('token');
      localStorage.removeItem('user');
      window.location.href = '/login';
    }
    
    const apiError: ApiError = {
      message: (error.response?.data as any)?.message || error.message || 'Erro desconhecido',
      status: error.response?.status || 0,
    };
    
    return Promise.reject(apiError);
  }
);

// Serviços da API

// Autenticação
export const authService = {
  async login(credentials: LoginRequestDTO): Promise<LoginResponseDTO> {
    const response = await api.post<LoginResponseDTO>('/usuarios/login', credentials);
    return response.data;
  },

  async refreshToken(): Promise<LoginResponseDTO> {
    const response = await api.post<LoginResponseDTO>('/usuarios/refresh-token');
    return response.data;
  },

  async validateToken(): Promise<{ valid: boolean }> {
    const response = await api.get<{ valid: boolean }>('/usuarios/validate-token');
    return response.data;
  },

  async getCurrentUser() {
    const response = await api.get('/usuarios/me');
    return response.data;
  }
};

// Usuários
export const usuarioService = {
  async getAll() {
    const response = await api.get('/usuarios');
    return response.data;
  },

  async getById(id: number) {
    const response = await api.get(`/usuarios/${id}`);
    return response.data;
  },

  async create(usuario: any) {
    const response = await api.post('/usuarios/registrar', usuario);
    return response.data;
  },

  async update(id: number, usuario: any) {
    const response = await api.put(`/usuarios/${id}`, usuario);
    return response.data;
  },

  async delete(id: number) {
    await api.delete(`/usuarios/${id}`);
  },

  async alterarSenha(id: number, senhas: any) {
    const response = await api.put(`/usuarios/${id}/alterar-senha`, senhas);
    return response.data;
  }
};

// Pacientes
export const pacienteService = {
  async getAll() {
    const response = await api.get('/pacientes');
    return response.data;
  },

  async getById(id: number) {
    const response = await api.get(`/pacientes/${id}`);
    return response.data;
  },

  async create(paciente: any) {
    const response = await api.post('/pacientes', paciente);
    return response.data;
  },

  async update(id: number, paciente: any) {
    const response = await api.put(`/pacientes/${id}`, paciente);
    return response.data;
  },

  async delete(id: number) {
    await api.delete(`/pacientes/${id}`);
  }
};

// Consultas
export const consultaService = {
  async getAll() {
    const response = await api.get('/consultas');
    return response.data;
  },

  async getById(id: number) {
    const response = await api.get(`/consultas/${id}`);
    return response.data;
  },

  async create(consulta: any) {
    const response = await api.post('/consultas', consulta);
    return response.data;
  },

  async update(id: number, consulta: any) {
    const response = await api.put(`/consultas/${id}`, consulta);
    return response.data;
  },

  async delete(id: number) {
    await api.delete(`/consultas/${id}`);
  },

  async getByPaciente(pacienteId: number) {
    const response = await api.get(`/consultas/paciente/${pacienteId}`);
    return response.data;
  },

  async getByMedico(medicoId: number) {
    const response = await api.get(`/consultas/medico/${medicoId}`);
    return response.data;
  }
};

export default api; 