import { Component, OnInit, inject, signal } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { PrestamosService } from '../../services/prestamos';
import { LibrosService } from '../../services/libros';
import { Auth } from '../../services/auth';

@Component({
  selector: 'app-prestamos',
  imports: [FormsModule],
  templateUrl: './prestamos.html',
  styleUrl: './prestamos.css',
})
export class Prestamos implements OnInit {
  private prestamosService = inject(PrestamosService);
  private librosService = inject(LibrosService);
  auth = inject(Auth);

  prestamos = signal<any[]>([]);
  libros = signal<any[]>([]);
  pagina = signal(0);
  totalPaginas = signal(0);
  aviso = signal('');

  libroId: number | null = null;
  fechaPrestamo = new Date().toLocaleDateString('en-CA');
  fechaDevolucion = '';

  ngOnInit(): void {
    this.listar(0);
    this.librosService.listar(0, 100).subscribe((resultado: any) => this.libros.set(resultado.content));
  }

  listar(numero: number): void {
    this.prestamosService.listar(numero).subscribe((resultado: any) => {
      this.prestamos.set(resultado.content);
      this.pagina.set(resultado.number);
      this.totalPaginas.set(resultado.totalPages);
    });
  }

  registrar(): void {
    if (!this.libroId) {
      this.aviso.set('Elegí un libro');
      return;
    }

    const cuerpo = {
      libroId: this.libroId,
      usuarioId: this.auth.id(),
      fechaPrestamo: this.fechaPrestamo,
      fechaDevolucion: this.fechaDevolucion || null,
      devuelto: false,
    };

    this.prestamosService.guardar(cuerpo).subscribe(() => {
      this.aviso.set('Préstamo registrado');
      this.libroId = null;
      this.fechaDevolucion = '';
      this.listar(0);
    });
  }

  // Actualizar: devolver el libro.
  // Se mandan solo los campos que espera el DTO, no el objeto completo.
  devolver(prestamo: any): void {
    const cuerpo = {
      id: prestamo.id,
      libroId: prestamo.libroId,
      usuarioId: prestamo.usuarioId,
      fechaPrestamo: prestamo.fechaPrestamo,
      fechaDevolucion: new Date().toLocaleDateString('en-CA'),
      devuelto: true,
    };

    this.prestamosService.guardar(cuerpo).subscribe(() => this.listar(this.pagina()));
  }

  eliminar(id: number): void {
    this.prestamosService.eliminar(id).subscribe(() => this.listar(this.pagina()));
  }
}
