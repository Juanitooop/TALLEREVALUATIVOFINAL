import { Component, OnInit, signal } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { JsonPipe } from '@angular/common';
import { AutoresService } from '../../services/autores';

@Component({
  selector: 'app-autores',
  imports: [FormsModule, JsonPipe],
  templateUrl: './autores.html',
  styleUrl: './autores.css',
})
export class Autores implements OnInit {
  autores = signal<any[]>([]);
  respuesta = signal<any>(null);
  pagina = signal(0);
  totalPaginas = signal(0);
  id = 1;
  cuerpo = '{"nombre":"Gabriel","apellido":"Garcia Marquez","nacionalidad":"Colombiana","fechaNacimiento":"1927-03-06"}';

  constructor(private autoresService: AutoresService) {}

  ngOnInit(): void {
    this.listar(0);
  }

  listar(numero: number): void {
    this.autoresService.listar(numero).subscribe((resultado: any) => {
      this.autores.set(resultado.content);
      this.pagina.set(resultado.number);
      this.totalPaginas.set(resultado.totalPages);
      this.respuesta.set(resultado);
    });
  }

  buscarUno(): void {
    this.autoresService.buscarUno(this.id).subscribe((resultado: any) => {
      this.respuesta.set(resultado);
    });
  }

  guardar(): void {
    this.autoresService.guardar(JSON.parse(this.cuerpo)).subscribe(() => {
      this.listar(0);
    });
  }

  eliminar(): void {
    this.autoresService.eliminar(this.id).subscribe(() => {
      this.listar(0);
    });
  }
}
