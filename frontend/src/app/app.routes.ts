import { Routes } from '@angular/router';
import { Main } from './components/main';
import { Dashboard } from './modules/dashboard/dashboard';
import { Autores } from './modules/autores/autores';
import { Libros } from './modules/libros/libros';
import { Usuarios } from './modules/usuarios/usuarios';
import { Prestamos } from './modules/prestamos/prestamos';

export const routes: Routes = [
    {
    path: '',
    component: Main,
    children: [
      { path: 'dashboard', component: Dashboard },
      { path: 'autores', component: Autores },
      { path: 'libros', component: Libros },
      { path: 'usuarios', component: Usuarios },
      { path: 'prestamos', component: Prestamos },
    ],
  },
];
