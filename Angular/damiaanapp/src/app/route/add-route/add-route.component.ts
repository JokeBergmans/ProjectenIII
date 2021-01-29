import { Component, OnInit, AfterViewInit } from '@angular/core';
import { NavigationExtras, Router } from '@angular/router';
import { EMPTY, pipe } from 'rxjs';
import { catchError } from 'rxjs/operators';
import { RoutingModule } from 'src/app/routing.module';
import { RouteData } from '../add-file/add-file.component';
import { Marker } from '../marker.model';
import { Route } from '../route.model';
import { RouteService } from '../route.service';
import { GeocodingService } from './geocoding.service';
import { MarkerService } from './marker.service';


@Component({
  selector: 'app-add-route',
  templateUrl: './add-route.component.html',
})

export class AddRouteComponent implements OnInit {
  
  damiaanactieLat = 50.86284580454369
  damiaanactieLng = 4.332647323608398

  private _kmlUrl : String;
  private _markers : Marker[] = new Array();
  private _map: google.maps.Map<HTMLElement>;
  private _routeId : number;
  private _routeName: string;  

  private _showSucces : boolean = false;
  private _succesMessage :string;

  private _showError : boolean = false; 
  private _errorMessage: string; 
  

  constructor(private _markerService: MarkerService,
    private _geocodingService: GeocodingService,
    private _routeService: RouteService,
    private _router: Router) { 
  }

  get markers() : Marker[] {
    return this._markers
  }

  get showSucces() {
    return this._showSucces
  }

  get succesMessage() : string {
    return this._succesMessage 
  }

  get routeName() : string {
    return this._routeName
  }

  get showError() {
    return this._showError
  }

  get errorMessage(): string {
    return this._errorMessage;
  }

  ngOnInit(): void {
    this.setInitialMap();
  }

  setInitialMap() {
    this._map = new google.maps.Map(document.getElementById("map") as HTMLElement, {
      center: { lat: this.damiaanactieLat, lng: this.damiaanactieLng },
      zoom: 8,
    }); 
  }

  addLabel(label: String, marker: Marker) {
    marker.label = label;
    this.addInfoWindow(marker.mapMarker, label)
  }
  
  //removes marker on right click 
  addDeleteListener(marker : Marker) {
    marker.mapMarker.addListener("rightclick", () => {
      this.deleteMarker(marker)
    })
  }

  addInfoWindow(marker: google.maps.Marker, content: String) {
    let infowindow = new google.maps.InfoWindow({
      content: content as string
    });

    marker.addListener("click", () => {
      infowindow.open(this._map, marker)
    })
  }

  clearLabel(marker: Marker) {
    marker.label = undefined;
    google.maps.event.clearListeners(marker.mapMarker, 'click')
  }

  addNewMarker(marker: Marker) {
    this._markers.push(marker)
  }
  
  addMarkerListeners() {
    
    this._map.addListener("click", (e: google.maps.MouseEvent) => {
      let lat = e.latLng.lat();
      let lon = e.latLng.lng(); 
      let marker = new Marker(lat, lon);   

      let mapMarker = new google.maps.Marker({
        position: {lat: lat, lng: lon},
        map: this._map
      })

      marker.mapMarker = mapMarker;
      this.addDeleteListener(marker);

      //cast to number tofixed returns string ?
      this._geocodingService.getAdress(Number(lat.toFixed(6)), Number(lon.toFixed(6))).subscribe(res => {        
        marker.street = res['address']['road']
        marker.number = res['address']['house_number']
        marker.city = res['address']['city']
        this.addNewMarker(marker);    
      })
    })
  }

  setRouteData(routeData : RouteData) {
    this._routeName = routeData.name;
    this._routeId = routeData.id;
    this.setKml(routeData.url)
    this._showSucces = false;
    this._showError = false;
  }

  setKml(url: String) {
    var kmlLayer = new google.maps.KmlLayer({
      url: url as string,
      suppressInfoWindows: true,
      map: this._map
    });
    this.addMarkerListeners();

    //kmlLayer.setMap(this._map)
  }

  getKml() {
    return this._kmlUrl
  }

  deleteMarker(marker: Marker) {
    marker.mapMarker.setMap(null)
    let index = this._markers.findIndex(m => m.latitude == marker.latitude && m.longitude == m.longitude)
    this._markers.splice(index, 1)
  }

  saveRoute() {
    this.markers.forEach(m => {
      this._markerService.addMarker(m, this._routeId).pipe(
        catchError(err => {
          this._errorMessage = `Er ging iets fout bij het opslaan van de route`
          this._showError = true; 
          return EMPTY;
        })
      ).subscribe();
    })
    this.showSuccesMessage(`De route ${this.routeName} werd succesvol opgeslagen.`)
    this.clearComponents();
  }

  clearComponents() {
    this.setInitialMap();
    this._markers = new Array();
  }

  deleteRoute() {
    this._markers
    this._routeService.deleteRoute(this._routeId).pipe(
      catchError(err => {
        this._errorMessage = `Er ging iets fout bij het verwijderen van de route`
        this._showError = true; 
        return EMPTY;
      })
    ).subscribe()
    this.showSuccesMessage(`De route ${this.routeName} werd succesvol verwijderd.`)
    this.clearComponents();
  }

  showSuccesMessage(msg: string) {
    if(!this._showError) {
      this._showSucces = true;
      this._succesMessage = msg;
    }
  }
}


