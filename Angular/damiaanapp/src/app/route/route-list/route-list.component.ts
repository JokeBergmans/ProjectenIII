import { Component, OnInit } from '@angular/core';
import { NavigationExtras, Router } from '@angular/router';
import { EMPTY, Observable } from 'rxjs';
import { catchError } from 'rxjs/operators';
import { MarkerService } from '../add-route/marker.service';
import { Route } from '../route.model';
import { RouteService } from '../route.service';

@Component({
  selector: 'app-route-list',
  templateUrl: './route-list.component.html',
  styleUrls: ['./route-list.component.css']
})
export class RouteListComponent implements OnInit {

  routes$ : Observable<Route[]>;
  errorMessage : string = '';
  showError : boolean = false; 
  today : number;
  showSucces : boolean = false;
  succesMessage :string;
  
  constructor(private _routeService : RouteService,
    private _router: Router,
    private _markerSerivce: MarkerService) { }

  ngOnInit(): void {
    this.getRoutes()
    this.today = Date.now();
  }

  getRoutes() {
    this.routes$ = this._routeService.getAllRoutes().pipe(
      catchError((err) => {
        this.errorMessage = err;
        return EMPTY;
      })     
    )
    this.routes$.subscribe();
  }

  createQRcodes(id : number, name: string) {    
    let navigationExtras: NavigationExtras = {
      queryParams: {
          name: name
      }
    }
    this._router.navigate([`${id}/qrcodes`], navigationExtras)
  }

  deleteRoute(id : number) {

    this._markerSerivce.deleteAllMarkers(id).pipe(
      catchError(err => {
        this.errorMessage = `Er ging iets fout bij het verwijderen van de route`
        this.showError = true; 
        return EMPTY;
      })
    ).subscribe()

    this._routeService.deleteRoute(id).pipe(
      catchError(err => {
        this.errorMessage = `Er ging iets fout bij het verwijderen van de route`
        this.showError = true; 
        return EMPTY;
      })
    ).subscribe()

    this.showSuccesMessage(`De route werd succesvol verwijderd.`)
    this.getRoutes()

  }

  notBeforeToday(start: string) {
    let startDate = Date.parse(start);
    return startDate > this.today;
  }

  showSuccesMessage(msg: string) {
    if(!this.showError) {
      this.showSucces = true;
      this.succesMessage = msg;
    }
  }
}
