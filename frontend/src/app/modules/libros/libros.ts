import { Component, OnInit, signal } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { JsonPipe } from '@angular/common';
import { LibrosService } from '../../services/libros';

@Component({
  selector: 'app-libros',
  imports: [FormsModule, JsonPipe],
  templateUrl: './libros.html',
  styleUrl: './libros.css',
})
export class Libros implements OnInit {
  libros = signal<any[]>([]);
  respuesta = signal<any>(null);
  pagina = signal(0);
  totalPaginas = signal(0);
  id = 1;
  texto = 'garcia';
  cuerpo = '{"titulo":"Rayuela","isbn":"222","anio":1963,"autorId":1}';

  constructor(private librosService: LibrosService) {}

  ngOnInit(): void {
    this.listar(0);
  }

  listar(numero: number): void {
    this.librosService.listar(numero).subscribe((resultado: any) => {
      this.libros.set(resultado.content);
      this.pagina.set(resultado.number);
      this.totalPaginas.set(resultado.totalPages);
      this.respuesta.set(resultado);
    });
  }

  buscar(): void {
    this.librosService.buscar(this.texto, 0).subscribe((resultado: any) => {
      this.libros.set(resultado.content);
      this.pagina.set(resultado.number);
      this.totalPaginas.set(resultado.totalPages);
      this.respuesta.set(resultado);
    });
  }

  buscarUno(): void {
    this.librosService.buscarUno(this.id).subscribe((resultado: any) => {
      this.respuesta.set(resultado);
    });
  }

  guardar(): void {
    this.librosService.guardar(JSON.parse(this.cuerpo)).subscribe(() => {
      this.listar(0);
    });
  }

  eliminar(): void {
    this.librosService.eliminar(this.id).subscribe(() => {
      this.listar(0);
    });
  }
}
