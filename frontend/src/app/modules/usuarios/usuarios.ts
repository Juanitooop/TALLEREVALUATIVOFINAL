import { Component, OnInit, signal } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { JsonPipe } from '@angular/common';
import { UsuariosService } from '../../services/usuarios';

@Component({
  selector: 'app-usuarios',
  imports: [FormsModule, JsonPipe],
  templateUrl: './usuarios.html',
  styleUrl: './usuarios.css',
})
export class Usuarios implements OnInit {
  usuarios = signal<any[]>([]);
  respuesta = signal<any>(null);
  pagina = signal(0);
  totalPaginas = signal(0);
  id = 1;
  texto = 'alber';
  cuerpo = '{"nombre":"Nuevo Lector","username":"nuevolector","email":"nuevo@correo.com","password":"12345678","rol":"LECTOR","estado":"ACTIVO"}';

  constructor(private usuariosService: UsuariosService) {}

  ngOnInit(): void {
    this.listar(0);
  }

  listar(numero: number): void {
    this.usuariosService.listar(numero).subscribe((resultado: any) => {
      this.usuarios.set(resultado.content);
      this.pagina.set(resultado.number);
      this.totalPaginas.set(resultado.totalPages);
      this.respuesta.set(resultado);
    });
  }

  buscar(): void {
    this.usuariosService.buscar(this.texto, 0).subscribe((resultado: any) => {
      this.usuarios.set(resultado.content);
      this.pagina.set(resultado.number);
      this.totalPaginas.set(resultado.totalPages);
      this.respuesta.set(resultado);
    });
  }

  buscarUno(): void {
    this.usuariosService.buscarUno(this.id).subscribe((resultado: any) => {
      this.respuesta.set(resultado);
    });
  }

  guardar(): void {
    this.usuariosService.guardar(JSON.parse(this.cuerpo)).subscribe(() => {
      this.listar(0);
    });
  }

  eliminar(): void {
    this.usuariosService.eliminar(this.id).subscribe(() => {
      this.listar(0);
    });
  }
}
