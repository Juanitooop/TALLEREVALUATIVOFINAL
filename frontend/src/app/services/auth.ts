import { Injectable, signal } from '@angular/core';
import { HttpClient } from '@angular/common/http';

@Injectable({ providedIn: 'root' })
export class Auth {
  private url = 'http://localhost:8080/auth/login';

  token = signal<string | null>(localStorage.getItem('token'));
  email = signal<string | null>(localStorage.getItem('email'));
  rol = signal<string | null>(localStorage.getItem('rol'));

  constructor(private http: HttpClient) {}

  login(email: string, password: string) {
    return this.http.post<{ token: string; email: string; rol: string }>(this.url, { email, password });
  }

  guardarSesion(respuesta: { token: string; email: string; rol: string }) {
    localStorage.setItem('token', respuesta.token);
    localStorage.setItem('email', respuesta.email);
    localStorage.setItem('rol', respuesta.rol);

    this.token.set(respuesta.token);
    this.email.set(respuesta.email);
    this.rol.set(respuesta.rol);
  }

  logout() {
    localStorage.clear();
    this.token.set(null);
    this.email.set(null);
    this.rol.set(null);
  }

  estaLogueado() {
    return this.token() !== null;
  }
}