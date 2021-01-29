import { HttpBackend, HttpClient, HttpHeaders } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { EMPTY } from 'rxjs';
import { catchError, tap } from 'rxjs/operators';
import { environment } from 'src/environments/environment';

@Injectable({
  providedIn: 'root'
})
export class GeocodingService {

  private httpClient: HttpClient;

  constructor(handler : HttpBackend) {
    this.httpClient = new HttpClient(handler);
   }

  
  private options = {
    headers: new HttpHeaders().set('Content-Type', 'application/json'),
  };

  public getAdress(lat: number, lon: number) {
    return this.httpClient.get(`${environment.locationIqUrl}&lat=${lat}&lon=${lon}&format=json`).pipe(
        catchError((err) => {
          console.log(err);
          return EMPTY;
        })
    )
  }
}
