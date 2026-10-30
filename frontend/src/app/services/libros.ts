import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';


//No debo olvidar como funciona. Mi angular llama a Java mediante peticiones HTTP. Java devuelve los datos en JSON. 
@Injectable({
  providedIn: 'root',
})
export class LibrosService {
  url = 'http://localhost:8080/libros'; //esta es mi direccion del backend, donde esta corriendo mi servidor de Java. !!!!!!

  constructor(private http: HttpClient) {}

  listar(pagina: number, size = 10) {
    return this.http.get(this.url + '?page=' + pagina + '&size=' + size);
  }

  buscar(texto: string, pagina: number) {
    return this.http.get(this.url + '/buscar?texto=' + encodeURIComponent(texto) + '&page=' + pagina + '&size=10');
  }

  buscarUno(id: number) {
    return this.http.get(this.url + '/' + id); //aqui busca !!!!
  }

  guardar(libro: any) {
    return this.http.post(this.url, libro); //aqui envia angular un libro para guardar !!!!!
  }

  eliminar(id: number) {
    return this.http.delete(this.url + '/' + id);
  }
}
