interface MarkerJson {
  id:string;
  latitude: number;
  longitude: number;
  label: String;
  street: String;
  number: String;
  city: String;
  info: String;
}


export class Marker {
  
  private _id: string; 
  private _label: String;
  private _street: String;
  private _number: String;
  private _city: String;
  private _mapMarker: google.maps.Marker;
  private _info: String


  constructor(private _latitude: number, private _longitude: number) {}


  static fromJSON(json: MarkerJson): Marker {
    const marker = new Marker(json.latitude, json.longitude);
    marker.id = String(json.id) as string;
    marker.street = json.street;
    marker.number = json.number;
    marker.city = json.city;
    marker.info = json.info;
    return marker;
  }

  toJson(): MarkerJson {
    return <MarkerJson>{
      latitude: this.latitude,
      longitude: this.longitude,
      label: this.label,
      street: this.street,
      number: this.number,
      city: this.city,
    };
  }
  
  public set info(info: String) {
    this._info = info;
  }

  public get info(): String {
    return this._info;
  }

  public set id(id: string) {
    this._id = id;
  }

  public get id(): string {
    return this._id;
  }
  public get latitude(): number {
    return this._latitude;
  }

  public set latitude(latitude: number) {
    this._latitude = latitude;
  }

  public get longitude(): number {
    return this._longitude;
  }

  public set longitude(longitude: number) {
    this._longitude = longitude;
  }

  public get label(): String {
    return this._label;
  }

  public set label(label: String) {
    this._label = label;
  }

  public get street(): String {
    return this._street;
  }

  public set street(street: String) {
    this._street = street;
  }

  public get number(): String {
    return this._number;
  }

  public set number(number: String) {
    this._number = number;
  }

  public get city(): String {
    return this._city;
  }

  public set city(city: String) {
    this._city = city;
  }

  public get mapMarker(): google.maps.Marker {
    return this._mapMarker;
  }

  public set mapMarker(mapMarker: google.maps.Marker) {
    this._mapMarker = mapMarker;
  }

  displayAddress() {
    let address = '';
    if (this.street) {
      address = this.street as string;
      if (this.number) address += ` ${this.number}`;
      if (this.city) address += `, ${this.city}`;
    } else {
      address = 'Ongeldig adres';
    }
    return address;
  }
}
