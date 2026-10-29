import { Routes } from '@angular/router';
import { Main } from './components/main';
import { Dashboard } from './modules/dashboard/dashboard';
import { Autores } from './modules/autores/autores';
import { Libros } from './modules/libros/libros';
import { Usuarios } from './modules/usuarios/usuarios';
import { Prestamos } from './modules/prestamos/prestamos';
import { Login } from './modules/login/login';

export const routes: Routes = [
  { path: 'login', component: Login },
  {
    path: '',
    component: Main,
    children: [
      { path: '', redirectTo: 'dashboard', pathMatch: 'full' },
      { path: 'dashboard', component: Dashboard },
      { path: 'autores', component: Autores },
      { path: 'libros', component: Libros },
      { path: 'usuarios', component: Usuarios },
      { path: 'prestamos', component: Prestamos },
    ],
  },
];
