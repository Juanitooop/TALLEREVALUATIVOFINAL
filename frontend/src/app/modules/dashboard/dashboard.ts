import { Component, OnInit, inject, signal } from '@angular/core';
import { RouterLink } from '@angular/router';
import { Auth } from '../../services/auth';
import { AutoresService } from '../../services/autores';
import { LibrosService } from '../../services/libros';
import { PrestamosService } from '../../services/prestamos';
import { UsuariosService } from '../../services/usuarios';

@Component({
  imports: [RouterLink],
  selector: 'app-dashboard',
  styleUrl: './dashboard.css',
  templateUrl: './dashboard.html',
})
export class Dashboard implements OnInit {
  auth = inject(Auth);
  private librosService = inject(LibrosService);
  private autoresService = inject(AutoresService);
  private usuariosService = inject(UsuariosService);
  private prestamosService = inject(PrestamosService);

  libros = signal(0);
  autores = signal(0);
  usuarios = signal(0);
  prestamos = signal(0);

  ngOnInit(): void {

    this.librosService.listar(0).subscribe((resultado: any) => this.libros.set(resultado.totalElements));
    this.autoresService.listar(0).subscribe((resultado: any) => this.autores.set(resultado.totalElements));
    this.prestamosService.listar(0).subscribe((resultado: any) => this.prestamos.set(resultado.totalElements));

    //Notita: usuarios es solo administrativo, asi que si el rol es lector ni lo pedimos
    if (this.auth.rol() === 'ADMIN') {
      this.usuariosService.listar(0).subscribe((resultado: any) => this.usuarios.set(resultado.totalElements));
    }
  }
}
