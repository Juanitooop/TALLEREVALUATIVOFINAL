import { Component, signal } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { Router } from '@angular/router';
import { Auth } from '../../services/auth';

@Component({
  selector: 'app-login',
  imports: [FormsModule],
  templateUrl: './login.html',
  styleUrl: './login.css',
})
export class Login {
  email = '';
  password = '';
  error = signal('');

  constructor(private auth: Auth, private router: Router) {}

  entrar(): void {
    this.error.set('');

    this.auth.login(this.email, this.password).subscribe({
      next: (respuesta) => {
        this.auth.guardarSesion(respuesta);
        this.router.navigate(['/dashboard']);
      },
      error: () => this.error.set('Email o contraseña incorrectos'),
    });
  }
}