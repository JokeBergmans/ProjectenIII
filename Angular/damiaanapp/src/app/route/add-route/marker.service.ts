import { HttpClient, HttpErrorResponse, HttpHeaders } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { EMPTY, Observable, throwError } from 'rxjs';
import { catchError, map, tap } from 'rxjs/operators';
import { LoginComponent } from 'src/app/user/login/login.component';
import { environment } from 'src/environments/environment';
import { Marker } from '../marker.model';

interface ScannedNode {
  timeScanned : Date
  node : Marker
}

@Injectable({
  providedIn: 'root',
})
export class MarkerService {
  constructor(private http: HttpClient) {}

  private options = {
    headers: new HttpHeaders().set(
      'Content-Type',
      'application/json; charset=utf-8'
    ),
  };

  addMarker(marker: Marker, routeId: number) {
    return this.http
      .post(
        `${environment.apiUrl}/routes/${routeId}/nodes`,
        {
          nodeType: 0,
          street: marker.street,
          number: marker.number,
          city: marker.city,
          postalCode: 9000,
          info: marker.label,
          longitude: marker.longitude,
          latitude: marker.latitude,
        },
        this.options
      )
      .pipe(
        tap(console.log), 
        catchError((err) => {
          console.log(err);
          return EMPTY;
        })
      )
  }

  getListScannedNodes(routeId : number, registrationId: number) : Observable<ScannedNode[]> {
     return this.http.get<ScannedNode[]>(`${environment.apiUrl}/Routes/${routeId}/Registrations/${registrationId}/Scanned/`).pipe(
      catchError(this.handleError),
      map((list: any[]): ScannedNode[] => list.map(l => l = {timeScanned : l.timeScanned, node : l.node })
    ));
  }

  getAllMarkersIdsFromRoute(routeId: string) : Observable<Marker[]> {
    return this.http.get(`${environment.apiUrl}/routes/${routeId}/nodes`).pipe(
      tap(res => console.log(res)),
      catchError(this.handleError),
      map((list: any[]): Marker[] => list.map(Marker.fromJSON))
    )
  }
  
  deleteAllMarkers(routeId: number) {
    return this.http.delete(`${environment.apiUrl}/routes/${routeId}/nodes`)
  }

  handleError(err: any): Observable<never> {
    let errorMessage: string;
    if (err instanceof HttpErrorResponse) {
      errorMessage = `'${err.status} ${err.statusText}' when accessing '${err.url}'`;
    } else {
      errorMessage = `an unknown error occurred ${err}`;
    }
    console.error(err);
    return throwError(errorMessage);
  }
}
