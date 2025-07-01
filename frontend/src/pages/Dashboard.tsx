import React, { useState, useEffect } from 'react';
import { Link } from 'react-router-dom';
import { 
  Users, 
  Calendar, 
  UserPlus, 
  Activity,
  TrendingUp,
  Clock,
  AlertCircle,
  CheckCircle
} from 'lucide-react';
import { useAuth } from '../context/AuthContext';
import { consultaService, pacienteService, usuarioService } from '../services/api';
import { formatDateTime } from '../utils';

interface DashboardStats {
  totalPacientes: number;
  totalConsultas: number;
  consultasHoje: number;
  consultasPendentes: number;
}

const Dashboard: React.FC = () => {
  const { user } = useAuth();
  const [stats, setStats] = useState<DashboardStats>({
    totalPacientes: 0,
    totalConsultas: 0,
    consultasHoje: 0,
    consultasPendentes: 0,
  });
  const [recentConsultas, setRecentConsultas] = useState<any[]>([]);
  const [isLoading, setIsLoading] = useState(true);

  useEffect(() => {
    const loadDashboardData = async () => {
      try {
        setIsLoading(true);
        
        // Carregar estatísticas
        const [pacientes, consultas] = await Promise.all([
          pacienteService.getAll(),
          consultaService.getAll(),
        ]);

        const hoje = new Date().toISOString().split('T')[0];
        const consultasHoje = consultas.filter((c: any) => 
          c.dataHora.startsWith(hoje)
        );
        const consultasPendentes = consultas.filter((c: any) => 
          c.status === 'AGENDADA' && new Date(c.dataHora) > new Date()
        );

        setStats({
          totalPacientes: pacientes.length,
          totalConsultas: consultas.length,
          consultasHoje: consultasHoje.length,
          consultasPendentes: consultasPendentes.length,
        });

        // Consultas recentes (últimas 5)
        const consultasRecentes = consultas
          .sort((a: any, b: any) => new Date(b.createdAt).getTime() - new Date(a.createdAt).getTime())
          .slice(0, 5);
        
        setRecentConsultas(consultasRecentes);
      } catch (error) {
        console.error('Erro ao carregar dados do dashboard:', error);
      } finally {
        setIsLoading(false);
      }
    };

    loadDashboardData();
  }, []);

  const statCards = [
    {
      title: 'Total de Pacientes',
      value: stats.totalPacientes,
      icon: Users,
      color: 'bg-blue-500',
      textColor: 'text-blue-600',
      bgColor: 'bg-blue-50',
      link: '/pacientes'
    },
    {
      title: 'Total de Consultas',
      value: stats.totalConsultas,
      icon: Calendar,
      color: 'bg-green-500',
      textColor: 'text-green-600',
      bgColor: 'bg-green-50',
      link: '/consultas'
    },
    {
      title: 'Consultas Hoje',
      value: stats.consultasHoje,
      icon: Clock,
      color: 'bg-orange-500',
      textColor: 'text-orange-600',
      bgColor: 'bg-orange-50',
      link: '/consultas'
    },
    {
      title: 'Consultas Pendentes',
      value: stats.consultasPendentes,
      icon: AlertCircle,
      color: 'bg-red-500',
      textColor: 'text-red-600',
      bgColor: 'bg-red-50',
      link: '/consultas'
    },
  ];

  const quickActions = [
    {
      title: 'Novo Paciente',
      description: 'Cadastrar novo paciente',
      icon: UserPlus,
      color: 'bg-blue-500',
      link: '/pacientes/novo'
    },
    {
      title: 'Nova Consulta',
      description: 'Agendar nova consulta',
      icon: Calendar,
      color: 'bg-green-500',
      link: '/consultas/nova'
    },
    {
      title: 'Ver Pacientes',
      description: 'Gerenciar pacientes',
      icon: Users,
      color: 'bg-purple-500',
      link: '/pacientes'
    },
    {
      title: 'Ver Consultas',
      description: 'Gerenciar consultas',
      icon: Activity,
      color: 'bg-indigo-500',
      link: '/consultas'
    },
  ];

  if (isLoading) {
    return (
      <div className="flex items-center justify-center h-64">
        <div className="loading-spinner w-8 h-8"></div>
        <span className="ml-2">Carregando...</span>
      </div>
    );
  }

  return (
    <div className="space-y-6">
      {/* Header */}
      <div className="flex justify-between items-center">
        <div>
          <h1 className="text-2xl font-bold text-gray-900">
            Bem-vindo, {user?.nome}!
          </h1>
          <p className="text-gray-600">
            {user?.role === 'MEDICO' ? 'Médico' : 'Enfermeira'} - {formatDateTime(new Date().toISOString())}
          </p>
        </div>
        <div className="flex items-center space-x-2">
          <div className="bg-green-100 text-green-800 px-3 py-1 rounded-full text-sm font-medium">
            Sistema Online
          </div>
        </div>
      </div>

      {/* Cards de Estatísticas */}
      <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-4 gap-6">
        {statCards.map((card, index) => {
          const Icon = card.icon;
          return (
            <Link
              key={index}
              to={card.link}
              className="card hover:shadow-lg transition-shadow duration-200"
            >
              <div className="card-content">
                <div className="flex items-center justify-between">
                  <div>
                    <p className="text-sm font-medium text-gray-600">{card.title}</p>
                    <p className="text-3xl font-bold text-gray-900">{card.value}</p>
                  </div>
                  <div className={`${card.bgColor} p-3 rounded-lg`}>
                    <Icon className={`w-6 h-6 ${card.textColor}`} />
                  </div>
                </div>
              </div>
            </Link>
          );
        })}
      </div>

      <div className="grid grid-cols-1 lg:grid-cols-2 gap-6">
        {/* Ações Rápidas */}
        <div className="card">
          <div className="card-header">
            <h2 className="text-lg font-semibold">Ações Rápidas</h2>
          </div>
          <div className="card-content">
            <div className="grid grid-cols-1 sm:grid-cols-2 gap-4">
              {quickActions.map((action, index) => {
                const Icon = action.icon;
                return (
                  <Link
                    key={index}
                    to={action.link}
                    className="flex items-center p-4 border rounded-lg hover:bg-gray-50 transition-colors"
                  >
                    <div className={`${action.color} p-2 rounded-lg mr-3`}>
                      <Icon className="w-5 h-5 text-white" />
                    </div>
                    <div>
                      <p className="font-medium text-gray-900">{action.title}</p>
                      <p className="text-sm text-gray-600">{action.description}</p>
                    </div>
                  </Link>
                );
              })}
            </div>
          </div>
        </div>

        {/* Consultas Recentes */}
        <div className="card">
          <div className="card-header">
            <h2 className="text-lg font-semibold">Consultas Recentes</h2>
          </div>
          <div className="card-content">
            {recentConsultas.length === 0 ? (
              <p className="text-gray-600 text-center py-4">
                Nenhuma consulta encontrada
              </p>
            ) : (
              <div className="space-y-3">
                {recentConsultas.map((consulta, index) => (
                  <div key={index} className="flex items-center justify-between p-3 bg-gray-50 rounded-lg">
                    <div className="flex items-center">
                      <div className="bg-blue-100 p-2 rounded-full mr-3">
                        <Calendar className="w-4 h-4 text-blue-600" />
                      </div>
                      <div>
                        <p className="font-medium text-gray-900">
                          {consulta.paciente?.nome || 'Paciente não informado'}
                        </p>
                        <p className="text-sm text-gray-600">
                          {formatDateTime(consulta.dataHora)}
                        </p>
                      </div>
                    </div>
                    <div className="flex items-center">
                      {consulta.status === 'AGENDADA' ? (
                        <div className="flex items-center text-orange-600">
                          <Clock className="w-4 h-4 mr-1" />
                          <span className="text-sm">Agendada</span>
                        </div>
                      ) : (
                        <div className="flex items-center text-green-600">
                          <CheckCircle className="w-4 h-4 mr-1" />
                          <span className="text-sm">Realizada</span>
                        </div>
                      )}
                    </div>
                  </div>
                ))}
              </div>
            )}
          </div>
        </div>
      </div>
    </div>
  );
};

export default Dashboard; 