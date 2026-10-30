import { Component, OnInit, inject, signal } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { LibrosService } from '../../services/libros';
import { Auth } from '../../services/auth';

@Component({
  selector: 'app-libros',
  imports: [FormsModule],
  templateUrl: './libros.html',
  styleUrl: './libros.css',
})
export class Libros implements OnInit {
  private servicio = inject(LibrosService);
  auth = inject(Auth);
  libros = signal<any[]>([]);
  pagina = signal(0);
  totalPaginas = signal(0);
  aviso = signal('');
  seleccionado = signal<any>(null);
  formulario = { id: null as number | null, titulo: '', isbn: '', anio: null as number | null, autorId: null as number | null };
  texto = '';
  filtro = '';

  ngOnInit(): void { this.listar(0); }

  listar(numero: number): void {
    const peticion = this.filtro ? this.servicio.buscar(this.filtro, numero) : this.servicio.listar(numero);
    peticion.subscribe({
      next: (resultado: any) => {
        this.libros.set(resultado.content);
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
    this.formulario = { id: null as number | null, titulo: '', isbn: '', anio: null as number | null, autorId: null as number | null };
    this.seleccionado.set(null);
  }

  editar(registro: any): void {
    this.formulario = {
      id: registro.id,
      titulo: registro.titulo ?? '',
      isbn: registro.isbn ?? '',
      anio: registro.anio ?? null,
      autorId: registro.autorId ?? null,
    };
    this.aviso.set('Editando el registro ' + registro.id);
  }

  guardar(): void {
    const datos = { ...this.formulario, isbn: this.formulario.isbn || null };
    this.servicio.guardar(datos).subscribe({
      next: () => {
        this.nuevo();
        this.aviso.set('Registro guardado.');
        this.listar(0);
      },
      error: () => this.aviso.set('No se pudo guardar. Revisa los campos, el ISBN y que exista el autor.'),
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
