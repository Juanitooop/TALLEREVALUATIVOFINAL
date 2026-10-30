import { Component, OnInit, inject, signal } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { AutoresService } from '../../services/autores';
import { Auth } from '../../services/auth';

@Component({
  selector: 'app-autores',
  imports: [FormsModule],
  templateUrl: './autores.html',
  styleUrl: './autores.css',
})
export class Autores implements OnInit {
  private servicio = inject(AutoresService);
  auth = inject(Auth);
  autores = signal<any[]>([]);
  pagina = signal(0);
  totalPaginas = signal(0);
  aviso = signal('');
  seleccionado = signal<any>(null);
  formulario = { id: null as number | null, nombre: '', apellido: '', nacionalidad: '', fechaNacimiento: '' };


  ngOnInit(): void { this.listar(0); }

  listar(numero: number): void {
    const peticion = this.servicio.listar(numero);
    peticion.subscribe({
      next: (resultado: any) => {
        this.autores.set(resultado.content);
        this.pagina.set(resultado.number);
        this.totalPaginas.set(resultado.totalPages);
      },
      error: () => this.aviso.set('No se pudo cargar la lista. Revisa la conexión y la sesión.'),
    });
  }

  nuevo(): void {
    this.formulario = { id: null as number | null, nombre: '', apellido: '', nacionalidad: '', fechaNacimiento: '' };
    this.seleccionado.set(null);
  }

  editar(registro: any): void {
    this.formulario = {
      id: registro.id,
      nombre: registro.nombre ?? '',
      apellido: registro.apellido ?? '',
      nacionalidad: registro.nacionalidad ?? '',
      fechaNacimiento: registro.fechaNacimiento ?? '',
    };
    this.aviso.set('Editando el registro ' + registro.id);
  }

  guardar(): void {
    const datos = { ...this.formulario, fechaNacimiento: this.formulario.fechaNacimiento || null };
    this.servicio.guardar(datos).subscribe({
      next: () => {
        this.nuevo();
        this.aviso.set('Registro guardado.');
        this.listar(0);
      },
      error: () => this.aviso.set('No se pudo guardar. Revisa los campos.'),
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
