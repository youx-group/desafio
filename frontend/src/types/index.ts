// Enums
export enum Role {
  MEDICO = 'MEDICO',
  ENFERMEIRA = 'ENFERMEIRA'
}

// Usuario
export interface Usuario {
  id: number;
  nome: string;
  cpf: string;
  role: Role;
  createdAt: string;
  updatedAt: string;
}

export interface UsuarioRequestDTO {
  nome: string;
  cpf: string;
  senha: string;
  role: Role;
}

export interface LoginRequestDTO {
  cpf: string;
  senha: string;
}

export interface LoginResponseDTO {
  token: string;
  usuario: Usuario;
}

export interface AlterarSenhaRequestDTO {
  senhaAtual: string;
  novaSenha: string;
}

// Paciente
export interface Paciente {
  id: number;
  nome: string;
  cpf: string;
  telefone?: string;
  dataDeNascimento: string;
  createdAt: string;
  updatedAt: string;
}

export interface PacienteRequestDTO {
  nome: string;
  cpf: string;
  telefone?: string;
  dataDeNascimento: string;
}

// Consulta
export interface Consulta {
  id: number;
  paciente: Paciente;
  medico: Usuario;
  dataHora: string;
  observacoes?: string;
  status?: string;
  createdAt: string;
  updatedAt: string;
}

export interface ConsultaRequestDTO {
  pacienteId: number;
  medicoId: number;
  dataHora: string;
  observacoes?: string;
  status?: string;
}

// Context
export interface AuthContextType {
  token: string | null;
  user: Usuario | null;
  login: (cpf: string, senha: string) => Promise<void>;
  logout: () => void;
  refreshToken: () => Promise<void>;
  isAuthenticated: boolean;
  isLoading: boolean;
}

// API Response
export interface ApiResponse<T> {
  data: T;
  message?: string;
}

export interface ApiError {
  message: string;
  status: number;
} 