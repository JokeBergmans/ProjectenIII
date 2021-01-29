import { Component, OnInit } from '@angular/core';
import { MatSnackBar } from '@angular/material/snack-bar';
import { EMPTY, Observable } from 'rxjs';
import { catchError, map } from 'rxjs/operators';
import { Route } from '../route/route.model';
import { RouteService } from '../route/route.service';
import { AuthenticationService } from '../user/authentication.service';

@Component({
  selector: 'app-welcome',
  templateUrl: './welcome.component.html',
})
export class WelcomeComponent implements OnInit {

  routes$ : Observable<Route[]>;
  registeredRoutes$ : Observable<Route[]>;
  loggedInUser$ = this._authenticationService.user$;
  errorMessage : string = '';
  registeredDays : Date[] = [];

  lat = 51.678418;
  lng = 7.809007;

  constructor(private _routeService : RouteService, 
    private _authenticationService: AuthenticationService, 
    private _snackbar: MatSnackBar) { }

  ngOnInit(): void {
    this.routes$ = this._routeService.getAllRoutes().pipe(
      catchError((err) => {
        this.errorMessage = err;
        this.opensnackbar();
        return EMPTY;
      })      
    );   
    if (this.loggedInUser$.value) {
      this.registeredRoutes$ = this._routeService.getRoutesForUser(Number(localStorage.getItem('userId'))).pipe(
        catchError((err) => {
          this.errorMessage = err;
          this.opensnackbar();
          return EMPTY;
        })
      );
      
      this.routes$.subscribe();

      this.registeredRoutes$.subscribe({
        next: r => {          
          r.forEach(route => this.registeredDays.push(route.start))
          this.routes$ = this.routes$.pipe(map(r => r.filter(route => !this.registeredDays.includes(route.start))));
        }
      });
    }
  }

  opensnackbar() {
    this._snackbar.open('Something went wrong!', '', {
      duration: 5000,
      panelClass: ['red-snackbar'],
    });
  }

}
