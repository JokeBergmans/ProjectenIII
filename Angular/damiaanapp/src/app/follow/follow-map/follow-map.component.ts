import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, NavigationEnd, Router } from '@angular/router';
import { MarkerService } from 'src/app/route/add-route/marker.service';
import { BehaviorSubject, EMPTY } from 'rxjs';
import { Marker } from 'src/app/route/marker.model';
import { Route } from '../../route/route.model';
import { RouteService } from 'src/app/route/route.service';
import { catchError, filter } from 'rxjs/operators';
import { environment } from 'src/environments/environment';
import { LoginComponent } from 'src/app/user/login/login.component';
import { HttpErrorResponse } from '@angular/common/http';

interface ScannedNode {
  timeScanned: Date
  node: Marker
}
@Component({
  selector: 'app-follow-map',
  templateUrl: './follow-map.component.html',
})
export class FollowMapComponent implements OnInit {

  private registrationId: number
  private _map: google.maps.Map<HTMLElement>;

  public route$: BehaviorSubject<any> = new BehaviorSubject(null);
  public canBeFollowed: boolean = null
  public loadedRoute: Route;
  public timestampLocation: Date
  private mapLoaded: boolean = false;
  private scannedNodes: ScannedNode[]
  private routeId: number

  constructor(private route: ActivatedRoute, private markerService: MarkerService, private routeService: RouteService, private router: Router) {
  }

  ngOnInit(): void {
    this.route.params.subscribe(param => {
      this.registrationId = param['registrationid'];
      this.routeId = param['routeid'];

      this.routeService.checkUserCanBeFollow(this.routeId, this.registrationId).subscribe(res => {
        this.canBeFollowed = res
        this.routeService.getRouteById(this.routeId).subscribe(route => {
          this.route$.next(route)
          this.loadedRoute = route;
          this.markerService.getListScannedNodes(this.routeId, this.registrationId).subscribe(nodes => {
            this.scannedNodes = nodes;
            this.getMarkersForRoute();
          }, (error: HttpErrorResponse) => {
            if (error.status == 404) {
              var span = document.getElementById("timestampSpan") as HTMLElement
              span.innerText = 'Deze persoon is nog niet aan de wandeling begonnen'
            }
          })
        },
        )
      },
        (error: HttpErrorResponse) => {
          console.log(error.status);
          if (error.status == 404) {
            this.canBeFollowed = false;
            this.route$.next(EMPTY)
          }
        });
    });
  }

  private getMarkersForRoute() {
    this.routeService.getMarkersForRoute(this.routeId).subscribe(val => {
      val.forEach(m => {
        let mapMarker = new google.maps.Marker({
          position: { lat: m.latitude, lng: m.longitude },
          icon: 'http://maps.google.com/mapfiles/ms/icons/red-dot.png',
        });
        if (this.scannedNodes.some(n => n.node.id == m.id)) {
          mapMarker.setIcon('http://maps.google.com/mapfiles/ms/icons/green-dot.png');
        }
        this.scannedNodes.forEach(node => {
          if (node.timeScanned > this.timestampLocation || this.timestampLocation == null) {
            this.timestampLocation = node.timeScanned;
          }
        })
        mapMarker.setMap(this._map);
      });
    });
  }

  ngDoCheck() {
    if (document.getElementById("map") as HTMLElement != null && this.mapLoaded == false && this.loadedRoute != null) {
      this.setInitialMap(this.loadedRoute);
      this.mapLoaded = true;
    }
  }

  private setInitialMap(route: Route) {
    this._map = new google.maps.Map(document.getElementById("map") as HTMLElement, {
      center: { lat: 50.8, lng: 4.3 },
      zoom: 10,
    });
    var url: string = `${environment.staticFilesUrl}/${this.loadedRoute.name}.kml`.replace(' ', '_')
    this.setKml(url)
  }
  private setKml(url: String) {
    var kmlLayer = new google.maps.KmlLayer({
      url: url as string,
      suppressInfoWindows: true,
      map: this._map
    });
  }

}
