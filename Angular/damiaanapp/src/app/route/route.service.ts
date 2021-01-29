import { HttpClient, HttpErrorResponse, HttpHeaders } from '@angular/common/http';
import { listLazyRoutes } from '@angular/compiler/src/aot/lazy_routes';
import { Injectable } from '@angular/core';
import { Router, RoutesRecognized } from '@angular/router';
import { EMPTY, Observable, throwError } from 'rxjs';
import { catchError, map, tap } from 'rxjs/operators';
import { environment } from 'src/environments/environment';
import { Marker } from './marker.model';
import { Registration } from './registration.model';
import { Route } from './route.model';

@Injectable({
  providedIn: 'root',
})
export class RouteService {
  constructor(private http: HttpClient, private router: Router) { }

  getAllRoutes(): Observable<Route[]> {
    return this.http.get(`${environment.apiUrl}/Routes/`).pipe(
      catchError(this.handleError),
      map((list: any[]): Route[] => list.map(Route.fromJSON))
    );
  }

  getRoutesForUser(id: number): Observable<Route[]> {
      return this.http.get(`${environment.apiUrl}/Participant/${id}/Registrations`).pipe(
        catchError(this.handleError),
        map((list: any[]): Route[] => list.map(l => Route.fromJSON(l.route)))
      );
  }
  getRegistrationsForRoute(routeId: number): Observable<any[]> {
    return this.http.get(`${environment.apiUrl}/Routes/${routeId}/Registrations`).pipe(
      catchError(this.handleError),
      map((val: any[]) => val.map(reg => reg.id)))
  }
  getRegistrationIdForUserOnRoute(userId : number, routeId : number) : Observable<any[]> {
    return this.http.get(`${environment.apiUrl}/Routes/${routeId}/Registrations`).pipe(
      catchError(this.handleError),
      map((val : any[]) => val.map(reg => {
        if(reg.participant.id == userId)
        return reg
      })))
  }

  getRouteById(id: number): Observable<Route> {
    return this.http.get(`${environment.apiUrl}/Routes/${id}`).pipe(
      catchError(this.handleError),
      map((val: any) => Route.fromJSON(val)
      ));
  }
  getMarkersForRoute(id: number): Observable<Marker[]> {
    return this.http.get<Marker[]>(`${environment.apiUrl}/Routes/${id}/Nodes`).pipe(
      catchError(this.handleError)

    );
  }

  checkUserCanBeFollow(routeId: number, registrationId: number): Observable<boolean> {
    return this.http.get<boolean>(`${environment.apiUrl}/Routes/${routeId}/Registrations/${registrationId}`).pipe(
      catchError(err => throwError(err)),
      map((val: any) => val.participant.canBeFollowed),
    )
  }

  addNewRegistration(id: number, registration: Registration): Observable<Registration> {
    return this.http
      .post(
        `${environment.apiUrl}/Routes/${id}/Registrations`,
        registration.toJSON(),
        { headers: new HttpHeaders().set('Content-Type', 'application/json') }
      )
      .pipe(catchError(this.handleError), map(Registration.fromJSON));
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

  deleteRoute(id: number): Observable<any> {
    return this.http.delete(`${environment.apiUrl}/Routes/${id}`);
  }
}
