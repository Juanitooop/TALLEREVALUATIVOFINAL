import { Component, OnInit, signal } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { JsonPipe } from '@angular/common';
import { PrestamosService } from '../../services/prestamos';

@Component({
  selector: 'app-prestamos',
  imports: [FormsModule, JsonPipe],
  templateUrl: './prestamos.html',
  styleUrl: './prestamos.css',
})
export class Prestamos implements OnInit {
  prestamos = signal<any[]>([]);
  respuesta = signal<any>(null);
  pagina = signal(0);
  totalPaginas = signal(0);
  id = 1;
  cuerpo = '{"libroId":1,"usuarioId":1,"fechaPrestamo":"2026-10-22","fechaDevolucion":null,"devuelto":false}';

  constructor(private prestamosService: PrestamosService) {}

  ngOnInit(): void {
    this.listar(0);
  }

  listar(numero: number): void {
    this.prestamosService.listar(numero).subscribe((resultado: any) => {
      this.prestamos.set(resultado.content);
      this.pagina.set(resultado.number);
      this.totalPaginas.set(resultado.totalPages);
      this.respuesta.set(resultado);
    });
  }

  buscarUno(): void {
    this.prestamosService.buscarUno(this.id).subscribe((resultado: any) => {
      this.respuesta.set(resultado);
    });
  }

  guardar(): void {
    this.prestamosService.guardar(JSON.parse(this.cuerpo)).subscribe(() => {
      this.listar(0);
    });
  }

  eliminar(): void {
    this.prestamosService.eliminar(this.id).subscribe(() => {
      this.listar(0);
    });
  }
}
