import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { NgxQrcodeElementTypes, NgxQrcodeErrorCorrectionLevels } from '@techiediaries/ngx-qrcode';
import { EMPTY, Observable } from 'rxjs';
import { catchError } from 'rxjs/operators';
import { MarkerService } from '../add-route/marker.service';
import { Marker } from '../marker.model';

@Component({
  selector: 'app-qrcodes',
  templateUrl: './qrcodes.component.html',
  styleUrls: ['./qrcodes.component.css']
})
export class QrcodesComponent implements OnInit {

  private _routeId: string; 
  private _routeName: string; 
  markers$ : Observable<Marker[]>;

  elementType = NgxQrcodeElementTypes.URL;
  correctionLevel = NgxQrcodeErrorCorrectionLevels.HIGH;
  value = "1";
  
  constructor(
    private _activatedRoute : ActivatedRoute,
    private _markerService: MarkerService) { }

  ngOnInit(): void {
      this._routeId = this._activatedRoute.snapshot.paramMap.get("id");
      this._activatedRoute.queryParams.subscribe(params => {
        this._routeName = params['name']
      });

      if(this._routeId) {
        this.markers$ = this._markerService.getAllMarkersIdsFromRoute(this._routeId).pipe(
          catchError(err => {
            console.log(err); 
            return EMPTY;
          })
        )
      }
      
      this.markers$.subscribe(res => {
        console.log(res)
        console.log(typeof res[0].id)
      })
  }

  get routeName() : string {
    return this._routeName
  }
}
