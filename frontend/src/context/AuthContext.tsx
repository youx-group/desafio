import React, { createContext, useContext, useState, useEffect } from 'react';
import { Usuario, AuthContextType } from '../types';
import { authService } from '../services/api';
import toast from 'react-hot-toast';

const AuthContext = createContext<AuthContextType | undefined>(undefined);

export const useAuth = () => {
  const context = useContext(AuthContext);
  if (context === undefined) {
    throw new Error('useAuth deve ser usado dentro de um AuthProvider');
  }
  return context;
};

interface AuthProviderProps {
  children: React.ReactNode;
}

export const AuthProvider: React.FC<AuthProviderProps> = ({ children }) => {
  const [token, setToken] = useState<string | null>(
    localStorage.getItem('token')
  );
  const [user, setUser] = useState<Usuario | null>(() => {
    const savedUser = localStorage.getItem('user');
    return savedUser ? JSON.parse(savedUser) : null;
  });
  const [isLoading, setIsLoading] = useState(false);

  const isAuthenticated = !!token && !!user;

  const login = async (cpf: string, senha: string) => {
    try {
      setIsLoading(true);
      const response = await authService.login({ cpf, senha });
      
      setToken(response.token);
      setUser(response.usuario);
      
      localStorage.setItem('tokenn', response.token);
      localStorage.setItem('user', JSON.stringify(response.usuario));
      
      toast.success('Login realizado com sucesso!');
    } catch (error: any) {
      toast.error(error.message || 'Erro ao fazer login');
      throw error;
    } finally {
      setIsLoading(false);
    }
  };

  const logout = () => {
    setToken(null);
    setUser(null);
    localStorage.removeItem('token');
    localStorage.removeItem('user');
    toast.success('Logout realizado com sucesso!');
  };

  const refreshToken = async () => {
    try {
      const response = await authService.refreshToken();
      setToken(response.token);
      setUser(response.usuario);
      localStorage.setItem('tokenn', response.token);
      localStorage.setItem('user', JSON.stringify(response.usuario));
    } catch (error) {
      logout();
      throw error;
    }
  };

  // Verificar se o token é válido na inicialização
  useEffect(() => {
    const validateToken = async () => {
      if (token) {
        try {
          await authService.validateToken();
        } catch (error) {
          logout();
        }
      }
    };

    validateToken();
  }, [token]);

  const value: AuthContextType = {
    token,
    user,
    login,
    logout,
    refreshToken,
    isAuthenticated,
    isLoading,
  };

  return <AuthContext.Provider value={value}>{children}</AuthContext.Provider>;
}; 