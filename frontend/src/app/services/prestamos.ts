import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';

@Injectable({
  providedIn: 'root',
})
export class PrestamosService {
  url = 'http://localhost:8080/prestamos';

  constructor(private http: HttpClient) {}

  listar(pagina: number) {
    return this.http.get(this.url + '?page=' + pagina + '&size=10');
  }

  buscarUno(id: number) {
    return this.http.get(this.url + '/' + id);
  }

  guardar(prestamo: any) {
    return this.http.post(this.url, prestamo);
  }

  eliminar(id: number) {
    return this.http.delete(this.url + '/' + id);
  }
}
