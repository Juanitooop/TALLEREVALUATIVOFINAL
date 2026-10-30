import { Component, OnInit, inject, signal } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { UsuariosService } from '../../services/usuarios';
import { Auth } from '../../services/auth';

@Component({
  selector: 'app-usuarios',
  imports: [FormsModule],
  templateUrl: './usuarios.html',
  styleUrl: './usuarios.css',
})
export class Usuarios implements OnInit {
  private servicio = inject(UsuariosService);
  auth = inject(Auth);
  usuarios = signal<any[]>([]);
  pagina = signal(0);
  totalPaginas = signal(0);
  aviso = signal('');
  seleccionado = signal<any>(null);
  formulario = { id: null as number | null, nombre: '', username: '', email: '', password: '', rol: 'LECTOR', estado: 'ACTIVO' };
  texto = '';
  filtro = '';

  ngOnInit(): void { this.listar(0); }

  listar(numero: number): void {
    const peticion = this.filtro ? this.servicio.buscar(this.filtro, numero) : this.servicio.listar(numero);
    peticion.subscribe({
      next: (resultado: any) => {
        this.usuarios.set(resultado.content);
        this.pagina.set(resultado.number);
        this.totalPaginas.set(resultado.totalPages);
      },
      error: () => this.aviso.set('No se pudo cargar la lista. Revisa la conexión y la sesión.'),
    });
  }

  buscar(): void {
    this.filtro = this.texto.trim();
    this.listar(0);
  }

  nuevo(): void {
    this.formulario = { id: null as number | null, nombre: '', username: '', email: '', password: '', rol: 'LECTOR', estado: 'ACTIVO' };
    this.seleccionado.set(null);
  }

  editar(registro: any): void {
    this.formulario = {
      id: registro.id,
      nombre: registro.nombre ?? '',
      username: registro.username ?? '',
      email: registro.email ?? '',
      password: '',
      rol: registro.rol ?? '',
      estado: registro.estado ?? '',
    };
    this.aviso.set('Editando el registro ' + registro.id);
  }

  guardar(): void {
    const datos = { ...this.formulario };
    this.servicio.guardar(datos).subscribe({
      next: () => {
        this.nuevo();
        this.aviso.set('Registro guardado.');
        this.listar(0);
      },
      error: () => this.aviso.set('No se pudo guardar. Revisa los campos y que el email no esté repetido.'),
    });
  }

  eliminar(id: number): void {
    if (!confirm('¿Eliminar el registro ' + id + '?')) return;
    this.servicio.eliminar(id).subscribe({
      next: () => {
        this.nuevo();
        this.aviso.set('Registro eliminado.');
        this.listar(0);
      },
      error: () => this.aviso.set('No se pudo eliminar. Puede tener registros relacionados o no tener permisos.'),
    });
  }
}
