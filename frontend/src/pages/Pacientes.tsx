import React, { useState, useEffect } from 'react';
import { Plus, Search, Edit, Trash2, Calendar, Phone, User, UserCheck } from 'lucide-react';
import { Paciente } from '../types';
import { pacienteService } from '../services/api';
import { formatCPF, formatPhone, formatDate, calculateAge } from '../utils';
import toast from 'react-hot-toast';
import Modal from '../components/Modal';
import PacienteForm from '../components/PacienteForm';

const Pacientes: React.FC = () => {
  const [pacientes, setPacientes] = useState<Paciente[]>([]);
  const [filteredPacientes, setFilteredPacientes] = useState<Paciente[]>([]);
  const [searchTerm, setSearchTerm] = useState('');
  const [isLoading, setIsLoading] = useState(true);
  const [showModal, setShowModal] = useState(false);
  const [showDeleteModal, setShowDeleteModal] = useState(false);
  const [editingPaciente, setEditingPaciente] = useState<Paciente | null>(null);
  const [deletingPaciente, setDeletingPaciente] = useState<Paciente | null>(null);

  // Carrega pacientes ao montar componente
  useEffect(() => {
    carregarPacientes();
  }, []);

  // Filtra pacientes com base no termo de busca
  useEffect(() => {
    if (!searchTerm) {
      setFilteredPacientes(pacientes);
    } else {
      const filtered = pacientes.filter(paciente =>
        paciente.nome.toLowerCase().includes(searchTerm.toLowerCase()) ||
        paciente.cpf.includes(searchTerm) ||
        paciente.telefone?.includes(searchTerm)
      );
      setFilteredPacientes(filtered);
    }
  }, [searchTerm, pacientes]);

  const carregarPacientes = async () => {
    try {
      setIsLoading(true);
      const data = await pacienteService.getAll();
      setPacientes(data);
    } catch (error) {
      console.error('Erro ao carregar pacientes:', error);
      toast.error('Erro ao carregar pacientes');
    } finally {
      setIsLoading(false);
    }
  };

  const handleNovoPaciente = () => {
    setEditingPaciente(null);
    setShowModal(true);
  };

  const handleEditarPaciente = (paciente: Paciente) => {
    setEditingPaciente(paciente);
    setShowModal(true);
  };

  const handleExcluirPaciente = (paciente: Paciente) => {
    setDeletingPaciente(paciente);
    setShowDeleteModal(true);
  };

  const confirmarExclusao = async () => {
    if (!deletingPaciente) return;

    try {
      await pacienteService.delete(deletingPaciente.id);
      toast.success('Paciente excluído com sucesso!');
      setShowDeleteModal(false);
      setDeletingPaciente(null);
      carregarPacientes();
    } catch (error) {
      console.error('Erro ao excluir paciente:', error);
      toast.error('Erro ao excluir paciente');
    }
  };

  const handleModalSuccess = () => {
    setShowModal(false);
    setEditingPaciente(null);
    carregarPacientes();
  };

  const handleModalCancel = () => {
    setShowModal(false);
    setEditingPaciente(null);
  };

  if (isLoading) {
    return (
      <div className="flex items-center justify-center min-h-96">
        <div className="loading-spinner w-8 h-8"></div>
      </div>
    );
  }

  return (
    <div className="space-y-6">
      {/* Header */}
      <div className="flex justify-between items-center">
        <div>
          <h1 className="text-2xl font-bold text-gray-900">Pacientes</h1>
          <p className="text-gray-600">Gerencie os pacientes da clínica</p>
        </div>
        <button
          onClick={handleNovoPaciente}
          className="btn-primary btn-md"
        >
          <Plus className="w-5 h-5 mr-2" />
          Novo Paciente
        </button>
      </div>

      {/* Barra de pesquisa */}
      <div className="card">
        <div className="card-content">
          <div className="relative">
            <Search className="absolute left-3 top-1/2 transform -translate-y-1/2 text-gray-400 w-5 h-5" />
            <input
              type="text"
              placeholder="Buscar por nome, CPF ou telefone..."
              className="input pl-10 w-full"
              value={searchTerm}
              onChange={(e) => setSearchTerm(e.target.value)}
            />
          </div>
        </div>
      </div>

      {/* Estatísticas */}
      <div className="grid grid-cols-1 md:grid-cols-3 gap-6">
        <div className="card">
          <div className="card-content">
            <div className="flex items-center">
              <div className="bg-blue-100 p-3 rounded-full">
                <User className="w-6 h-6 text-blue-600" />
              </div>
              <div className="ml-4">
                <p className="text-sm font-medium text-gray-600">Total de Pacientes</p>
                <p className="text-2xl font-bold text-gray-900">{pacientes.length}</p>
              </div>
            </div>
          </div>
        </div>

        <div className="card">
          <div className="card-content">
            <div className="flex items-center">
              <div className="bg-green-100 p-3 rounded-full">
                <UserCheck className="w-6 h-6 text-green-600" />
              </div>
              <div className="ml-4">
                <p className="text-sm font-medium text-gray-600">Pacientes Ativos</p>
                <p className="text-2xl font-bold text-gray-900">{filteredPacientes.length}</p>
              </div>
            </div>
          </div>
        </div>

        <div className="card">
          <div className="card-content">
            <div className="flex items-center">
              <div className="bg-purple-100 p-3 rounded-full">
                <Calendar className="w-6 h-6 text-purple-600" />
              </div>
              <div className="ml-4">
                <p className="text-sm font-medium text-gray-600">Cadastros Hoje</p>
                <p className="text-2xl font-bold text-gray-900">
                  {pacientes.filter(p => 
                    new Date(p.createdAt).toDateString() === new Date().toDateString()
                  ).length}
                </p>
              </div>
            </div>
          </div>
        </div>
      </div>

      {/* Lista de pacientes */}
      <div className="card">
        <div className="card-header">
          <h2 className="text-lg font-semibold">
            Lista de Pacientes ({filteredPacientes.length})
          </h2>
        </div>
        <div className="card-content p-0">
          {filteredPacientes.length === 0 ? (
            <div className="text-center py-12">
              <User className="w-12 h-12 text-gray-400 mx-auto mb-4" />
              <h3 className="text-lg font-medium text-gray-900 mb-2">
                {searchTerm ? 'Nenhum paciente encontrado' : 'Nenhum paciente cadastrado'}
              </h3>
              <p className="text-gray-500 mb-4">
                {searchTerm 
                  ? 'Tente buscar com outros termos'
                  : 'Comece cadastrando o primeiro paciente da clínica'
                }
              </p>
              {!searchTerm && (
                <button
                  onClick={handleNovoPaciente}
                  className="btn-primary btn-md"
                >
                  <Plus className="w-5 h-5 mr-2" />
                  Cadastrar Primeiro Paciente
                </button>
              )}
            </div>
          ) : (
            <div className="overflow-x-auto">
              <table className="table">
                <thead className="table-header">
                  <tr>
                    <th className="table-head">Nome</th>
                    <th className="table-head">CPF</th>
                    <th className="table-head">Idade</th>
                    <th className="table-head">Telefone</th>
                    <th className="table-head">Cadastro</th>
                    <th className="table-head">Ações</th>
                  </tr>
                </thead>
                <tbody className="table-body">
                  {filteredPacientes.map((paciente) => (
                    <tr key={paciente.id} className="table-row">
                      <td className="table-cell">
                        <div className="flex items-center">
                          <div className="avatar">
                            <div className="avatar-fallback">
                              {paciente.nome.charAt(0).toUpperCase()}
                            </div>
                          </div>
                          <div className="ml-4">
                            <div className="text-sm font-medium text-gray-900">
                              {paciente.nome}
                            </div>
                          </div>
                        </div>
                      </td>
                      <td className="table-cell">
                        <span className="text-sm text-gray-900 font-mono">
                          {formatCPF(paciente.cpf)}
                        </span>
                      </td>
                      <td className="table-cell">
                        <span className="text-sm text-gray-900">
                          {calculateAge(paciente.dataDeNascimento)} anos
                        </span>
                      </td>
                      <td className="table-cell">
                        {paciente.telefone ? (
                          <div className="flex items-center text-sm text-gray-900">
                            <Phone className="w-4 h-4 mr-1 text-gray-400" />
                            {formatPhone(paciente.telefone)}
                          </div>
                        ) : (
                          <span className="text-sm text-gray-500">-</span>
                        )}
                      </td>
                      <td className="table-cell">
                        <span className="text-sm text-gray-500">
                          {formatDate(paciente.createdAt)}
                        </span>
                      </td>
                      <td className="table-cell">
                        <div className="flex items-center space-x-2">
                          <button
                            onClick={() => handleEditarPaciente(paciente)}
                            className="btn-ghost btn-sm"
                            title="Editar paciente"
                          >
                            <Edit className="w-4 h-4" />
                          </button>
                          <button
                            onClick={() => handleExcluirPaciente(paciente)}
                            className="btn-ghost btn-sm text-red-600 hover:text-red-700 hover:bg-red-50"
                            title="Excluir paciente"
                          >
                            <Trash2 className="w-4 h-4" />
                          </button>
                        </div>
                      </td>
                    </tr>
                  ))}
                </tbody>
              </table>
            </div>
          )}
        </div>
      </div>

      {/* Modal de cadastro/edição */}
      <Modal
        isOpen={showModal}
        onClose={handleModalCancel}
        title={editingPaciente ? 'Editar Paciente' : 'Novo Paciente'}
        size="md"
      >
        <PacienteForm
          paciente={editingPaciente}
          onSuccess={handleModalSuccess}
          onCancel={handleModalCancel}
        />
      </Modal>

      {/* Modal de confirmação de exclusão */}
      <Modal
        isOpen={showDeleteModal}
        onClose={() => setShowDeleteModal(false)}
        title="Confirmar Exclusão"
        size="sm"
      >
        <div className="space-y-4">
          <p className="text-gray-600">
            Tem certeza que deseja excluir o paciente <strong>{deletingPaciente?.nome}</strong>?
          </p>
          <p className="text-sm text-red-600">
            Esta ação não pode ser desfeita.
          </p>
          <div className="flex gap-3 pt-4">
            <button
              onClick={() => setShowDeleteModal(false)}
              className="btn-secondary btn-md flex-1"
            >
              Cancelar
            </button>
            <button
              onClick={confirmarExclusao}
              className="btn-destructive btn-md flex-1"
            >
              Excluir
            </button>
          </div>
        </div>
      </Modal>
    </div>
  );
};

export default Pacientes; 