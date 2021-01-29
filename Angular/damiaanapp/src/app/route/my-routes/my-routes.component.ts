import { Component, OnInit } from '@angular/core';
import { MatSnackBar } from '@angular/material/snack-bar';
import { Router } from '@angular/router';
import { EMPTY, Observable } from 'rxjs';
import { catchError, tap } from 'rxjs/operators';
import { AuthenticationService } from 'src/app/user/authentication.service';
import { Route } from '../route.model';
import { RouteService } from '../route.service';

@Component({
  selector: 'app-my-routes',
  templateUrl: './my-routes.component.html',
})
export class MyRoutesComponent implements OnInit {

  routes$ : Observable<Route[]>;
  emptyRoutes : boolean = false;
  errorMessage : string = '';

  constructor(private _routeService : RouteService, 
    private _snackbar : MatSnackBar,
    private _router: Router) { }

  ngOnInit(): void {   
    this.routes$ = this._routeService.getRoutesForUser(Number(localStorage.getItem('userId'))).pipe(
      tap(res => console.log(res)),
      catchError((err) => {
        this.errorMessage = err;
        this.opensnackbar();
        return EMPTY;
      })
    );

    this.routes$.subscribe();
  }

  showDetail(id: String) {
    this._router.navigate([`route/${id}/detail`]); 
  }

  opensnackbar() {
    this._snackbar.open('Something went wrong!', '', {
      duration: 5000,
      panelClass: ['red-snackbar'],
    });
  }
}
