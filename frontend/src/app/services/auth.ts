import { Injectable, signal } from '@angular/core';
import { HttpClient } from '@angular/common/http';

@Injectable({ providedIn: 'root' })
export class Auth {
  private url = 'http://localhost:8080/auth/login';

  token = signal<string | null>(localStorage.getItem('token'));
  id = signal<number | null>(this.leerId());
  email = signal<string | null>(localStorage.getItem('email'));
  rol = signal<string | null>(localStorage.getItem('rol'));

  constructor(private http: HttpClient) {}

  login(email: string, password: string) {
    return this.http.post<{ id: number; token: string; email: string; rol: string }>(this.url, {
      email,
      password,
    });
  }

  guardarSesion(respuesta: { id: number; token: string; email: string; rol: string }) {
    localStorage.setItem('id', String(respuesta.id));
    localStorage.setItem('token', respuesta.token);
    localStorage.setItem('email', respuesta.email);
    localStorage.setItem('rol', respuesta.rol);

    this.id.set(respuesta.id);
    this.token.set(respuesta.token);
    this.email.set(respuesta.email);
    this.rol.set(respuesta.rol);
  }

  logout() {
    localStorage.clear();
    this.id.set(null);
    this.token.set(null);
    this.email.set(null);
    this.rol.set(null);
  }

  estaLogueado() {
    return this.token() !== null;
  }

  private leerId(): number | null {
    const id = localStorage.getItem('id');
    return id ? Number(id) : null;
  }
}
