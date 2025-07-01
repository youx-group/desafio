import React from 'react';
import { Routes, Route, Navigate } from 'react-router-dom';
import { useAuth } from './context/AuthContext';
import Layout from './components/Layout';
import Login from './pages/Login';
import Dashboard from './pages/Dashboard';
import Pacientes from './pages/Pacientes';

// Componente para proteger rotas
const ProtectedRoute: React.FC<{ children: React.ReactNode }> = ({ children }) => {
  const { isAuthenticated, isLoading } = useAuth();

  if (isLoading) {
    return (
      <div className="min-h-screen flex items-center justify-center">
        <div className="loading-spinner w-8 h-8"></div>
        <span className="ml-2">Carregando...</span>
      </div>
    );
  }

  return isAuthenticated ? <>{children}</> : <Navigate to="/login" replace />;
};

// Componente para rotas públicas (redireciona se já autenticado)
const PublicRoute: React.FC<{ children: React.ReactNode }> = ({ children }) => {
  const { isAuthenticated, isLoading } = useAuth();

  if (isLoading) {
    return (
      <div className="min-h-screen flex items-center justify-center">
        <div className="loading-spinner w-8 h-8"></div>
        <span className="ml-2">Carregando...</span>
      </div>
    );
  }

  return !isAuthenticated ? <>{children}</> : <Navigate to="/" replace />;
};

const App: React.FC = () => {
  return (
    <Routes>
      {/* Rota de Login */}
      <Route
        path="/login"
        element={
          <PublicRoute>
            <Login />
          </PublicRoute>
        }
      />

      {/* Rotas Protegidas */}
      <Route
        path="/*"
        element={
          <ProtectedRoute>
            <Layout>
              <Routes>
                <Route path="/" element={<Dashboard />} />
                <Route path="/dashboard" element={<Dashboard />} />
                
                {/* Rotas de Pacientes */}
                <Route path="/pacientes" element={<Pacientes />} />

                {/* Rotas de Consultas */}
                <Route 
                  path="/consultas" 
                  element={
                    <div className="text-center py-8">
                      <h2 className="text-2xl font-bold text-gray-900">Gerenciar Consultas</h2>
                      <p className="text-gray-600 mt-2">Esta funcionalidade será implementada em breve.</p>
                    </div>
                  } 
                />
                <Route 
                  path="/consultas/nova" 
                  element={
                    <div className="text-center py-8">
                      <h2 className="text-2xl font-bold text-gray-900">Nova Consulta</h2>
                      <p className="text-gray-600 mt-2">Esta funcionalidade será implementada em breve.</p>
                    </div>
                  } 
                />

                {/* Rotas de Usuários */}
                <Route 
                  path="/usuarios" 
                  element={
                    <div className="text-center py-8">
                      <h2 className="text-2xl font-bold text-gray-900">Gerenciar Usuários</h2>
                      <p className="text-gray-600 mt-2">Esta funcionalidade será implementada em breve.</p>
                    </div>
                  } 
                />

                {/* Perfil */}
                <Route 
                  path="/perfil" 
                  element={
                    <div className="text-center py-8">
                      <h2 className="text-2xl font-bold text-gray-900">Meu Perfil</h2>
                      <p className="text-gray-600 mt-2">Esta funcionalidade será implementada em breve.</p>
                    </div>
                  } 
                />

                {/* Rota 404 */}
                <Route 
                  path="*" 
                  element={
                    <div className="text-center py-8">
                      <h2 className="text-2xl font-bold text-gray-900">Página não encontrada</h2>
                      <p className="text-gray-600 mt-2">A página que você procura não existe.</p>
                    </div>
                  } 
                />
              </Routes>
            </Layout>
          </ProtectedRoute>
        }
      />
    </Routes>
  );
};

export default App; 