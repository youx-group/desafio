import React, { useState } from 'react';
import { Link, useLocation } from 'react-router-dom';
import { 
  Menu, 
  X, 
  Home, 
  Users, 
  Calendar, 
  UserCog, 
  User, 
  LogOut,
  Activity,
  Bell,
  Settings
} from 'lucide-react';
import { useAuth } from '../context/AuthContext';
import { getInitials, stringToColor } from '../utils';

interface LayoutProps {
  children: React.ReactNode;
}

const Layout: React.FC<LayoutProps> = ({ children }) => {
  const [sidebarOpen, setSidebarOpen] = useState(false);
  const { user, logout } = useAuth();
  const location = useLocation();

  const navigation = [
    {
      name: 'Dashboard',
      href: '/',
      icon: Home,
      current: location.pathname === '/' || location.pathname === '/dashboard'
    },
    {
      name: 'Pacientes',
      href: '/pacientes',
      icon: Users,
      current: location.pathname.startsWith('/pacientes')
    },
    {
      name: 'Consultas',
      href: '/consultas',
      icon: Calendar,
      current: location.pathname.startsWith('/consultas')
    },
    ...(user?.role === 'MEDICO' ? [{
      name: 'Usuários',
      href: '/usuarios',
      icon: UserCog,
      current: location.pathname.startsWith('/usuarios')
    }] : []),
  ];

  const handleLogout = () => {
    logout();
  };

  return (
    <div className="flex h-screen bg-gray-50">
      {/* Sidebar */}
      <div className={`fixed inset-y-0 left-0 z-50 w-64 bg-white shadow-lg transform ${
        sidebarOpen ? 'translate-x-0' : '-translate-x-full'
      } transition-transform duration-300 ease-in-out lg:translate-x-0 lg:static lg:inset-0`}>
        
        {/* Logo */}
        <div className="flex items-center justify-center h-16 px-4 bg-blue-600">
          <div className="flex items-center">
            <Activity className="w-8 h-8 text-white" />
            <span className="ml-2 text-xl font-bold text-white">Clínica</span>
          </div>
        </div>

        {/* Navigation */}
        <nav className="mt-5 px-2">
          <div className="space-y-1">
            {navigation.map((item) => {
              const Icon = item.icon;
              return (
                <Link
                  key={item.name}
                  to={item.href}
                  className={`${
                    item.current
                      ? 'bg-blue-100 border-blue-500 text-blue-700'
                      : 'border-transparent text-gray-600 hover:bg-gray-50 hover:text-gray-900'
                  } group flex items-center px-2 py-2 text-sm font-medium border-l-4 transition-colors`}
                  onClick={() => setSidebarOpen(false)}
                >
                  <Icon
                    className={`${
                      item.current ? 'text-blue-500' : 'text-gray-400 group-hover:text-gray-500'
                    } mr-3 h-6 w-6`}
                    aria-hidden="true"
                  />
                  {item.name}
                </Link>
              );
            })}
          </div>
        </nav>

        {/* User info at bottom */}
        <div className="absolute bottom-0 w-full p-4 border-t border-gray-200">
          <div className="flex items-center">
            <div 
              className="flex-shrink-0 w-10 h-10 rounded-full flex items-center justify-center text-white text-sm font-medium"
              style={{ backgroundColor: stringToColor(user?.nome || '') }}
            >
              {getInitials(user?.nome || '')}
            </div>
            <div className="ml-3 flex-1 min-w-0">
              <p className="text-sm font-medium text-gray-900 truncate">
                {user?.nome}
              </p>
              <p className="text-xs text-gray-500">
                {user?.role === 'MEDICO' ? 'Médico' : 'Enfermeira'}
              </p>
            </div>
          </div>
          
          <div className="mt-3 flex space-x-2">
            <Link
              to="/perfil"
              className="flex-1 btn-outline btn-sm text-center"
              onClick={() => setSidebarOpen(false)}
            >
              <User className="w-4 h-4 mr-1" />
              Perfil
            </Link>
            <button
              onClick={handleLogout}
              className="flex-1 btn-secondary btn-sm"
            >
              <LogOut className="w-4 h-4 mr-1" />
              Sair
            </button>
          </div>
        </div>
      </div>

      {/* Overlay for mobile */}
      {sidebarOpen && (
        <div 
          className="fixed inset-0 z-40 bg-gray-600 bg-opacity-75 lg:hidden"
          onClick={() => setSidebarOpen(false)}
        />
      )}

      {/* Main content */}
      <div className="flex-1 flex flex-col overflow-hidden">
        {/* Top bar */}
        <header className="bg-white shadow-sm border-b border-gray-200">
          <div className="flex items-center justify-between px-4 py-3">
            <div className="flex items-center">
              <button
                type="button"
                className="text-gray-500 hover:text-gray-700 lg:hidden"
                onClick={() => setSidebarOpen(true)}
              >
                <Menu className="w-6 h-6" />
              </button>
              
              <div className="ml-4 lg:ml-0">
                <h1 className="text-lg font-semibold text-gray-900">
                  Sistema Clínico
                </h1>
              </div>
            </div>

            <div className="flex items-center space-x-3">
              {/* Notifications */}
              <button className="p-2 text-gray-400 hover:text-gray-500 relative">
                <Bell className="w-6 h-6" />
                <span className="absolute top-0 right-0 block h-3 w-3 rounded-full bg-red-400 ring-2 ring-white" />
              </button>

              {/* Settings */}
              <button className="p-2 text-gray-400 hover:text-gray-500">
                <Settings className="w-6 h-6" />
              </button>

              {/* User avatar */}
              <div className="flex items-center">
                <div 
                  className="w-8 h-8 rounded-full flex items-center justify-center text-white text-sm font-medium"
                  style={{ backgroundColor: stringToColor(user?.nome || '') }}
                >
                  {getInitials(user?.nome || '')}
                </div>
              </div>
            </div>
          </div>
        </header>

        {/* Page content */}
        <main className="flex-1 overflow-y-auto">
          <div className="p-6">
            {children}
          </div>
        </main>
      </div>
    </div>
  );
};

export default Layout; 