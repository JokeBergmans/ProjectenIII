import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { EMPTY } from 'rxjs';
import { catchError, tap } from 'rxjs/operators';
import { environment } from 'src/environments/environment';

@Injectable({
  providedIn: 'root',
})
export class KmlFileService {
  constructor(private http: HttpClient) {}

  private options = {
    headers: new HttpHeaders().set('Content-Type', 'application/json'),
  };

  public addKMLfile(name: String, afstand: number, startDate: Date, baseString: string | ArrayBuffer, url : String, info: String) {
    return this.http.post(`${environment.apiUrl}/routes`, {name: name, distance: afstand, start: startDate, kml: baseString, url: url, info: info}, this.options)
      .pipe(
        tap(console.log),
        catchError((err) => {
          console.log(err);
          return EMPTY;
        })
      );
  }
}
