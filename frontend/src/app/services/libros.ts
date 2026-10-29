import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';

@Injectable({
  providedIn: 'root',
})
export class LibrosService {
  url = 'http://localhost:8080/libros';

  constructor(private http: HttpClient) {}

  listar(pagina: number, size = 10) {
    return this.http.get(this.url + '?page=' + pagina + '&size=' + size);
  }

  buscar(texto: string, pagina: number) {
    return this.http.get(this.url + '/buscar?texto=' + texto + '&page=' + pagina + '&size=10');
  }

  buscarUno(id: number) {
    return this.http.get(this.url + '/' + id);
  }

  guardar(libro: any) {
    return this.http.post(this.url, libro);
  }

  eliminar(id: number) {
    return this.http.delete(this.url + '/' + id);
  }
}
