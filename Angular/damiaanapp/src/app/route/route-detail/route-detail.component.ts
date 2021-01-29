import { Component, OnInit } from '@angular/core';
import { MatSnackBar } from '@angular/material/snack-bar';
import { ActivatedRoute } from '@angular/router';
import { EMPTY, Observable, pipe } from 'rxjs';
import { catchError, tap } from 'rxjs/operators';
import { MarkerService } from '../add-route/marker.service';
import { Marker } from '../marker.model';
import { Route } from '../route.model';
import { RouteService } from '../route.service';

@Component({
  selector: 'app-route-detail',
  templateUrl: './route-detail.component.html',
  styleUrls: ['./route-detail.component.css']
})
export class RouteDetailComponent implements OnInit {
  
  private _routeId: string;

  
  route$ : Observable<Route>; 
  nodes$ : Observable<Marker[]>
  registrationId : number
  errorMessage : string = '';

  constructor(
    private _activatedRoute : ActivatedRoute,
    private _routeService : RouteService,
    private _snackbar : MatSnackBar,
    private _markerService: MarkerService) { }

  ngOnInit(): void {
    
    this._routeId = this._activatedRoute.snapshot.paramMap.get("id");

    if(this._routeId) {
      this.route$ = this._routeService.getRouteById(Number(this._routeId)).pipe(
        catchError((err) => {
          this.errorMessage = err;
          this.opensnackbar();
          return EMPTY;
        })
      );
 
      this._routeService.getRegistrationIdForUserOnRoute(Number(localStorage.getItem('userId')),Number(this._routeId)).subscribe(val => {
        this.registrationId = val.filter(item => item !== undefined)[0].id
      })

      this.nodes$ = this._markerService.getAllMarkersIdsFromRoute(this._routeId).pipe(
        tap(res => console.log(res)),
        catchError((err) => {
          this.errorMessage = err;
          this.opensnackbar();
          return EMPTY;
        })
      )
    }

    this.route$.subscribe()
    this.nodes$.subscribe()

  }

  opensnackbar() {
    this._snackbar.open('Something went wrong!', '', {
      duration: 5000,
      panelClass: ['red-snackbar'],
    });
  }
}
