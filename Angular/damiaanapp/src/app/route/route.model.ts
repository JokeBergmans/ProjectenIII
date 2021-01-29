import { VirtualTimeScheduler } from 'rxjs';

interface RouteDTOJson {
    id: number;
    name: string;
    distance: number;
    start: Date;
    url: String; 
    info: string;
}
interface Node {
  id : number;
  latitude : number;
  longitude : number;
}

export class Route {

private _nodes : Node[];

    private _url: String

    constructor(
        private _id : number,
        private _name : string,
        private _distance : number,
        private _start : Date,
        private _info : string
    ) {}


    public get info() {
      return this._info;
    }

    public set info(info : string) {
      this._info = info;
    }

    public get nodes() : Node[] {
      return this._nodes
    }

    public set nodes(nodes : Node[]) {
      this._nodes = nodes;
    }

    public set url(url : String) {
      this._url = url;
    }
    
    public get url(): String {
      return this._url;
    }

    public get id() : number {
        return this._id;
    }
    public get name() : string {
        return this._name.replace(/_/g,' ');
    }

    public get distance() : number {
        return this._distance;
    }

    public get start() : Date {
        return this._start;
    }

    static fromJSON(json: RouteDTOJson): Route {
        const route = new Route(
          json.id,
          json.name,
          json.distance,
          json.start,
          json.info
        );
        route.url = json.url;
        return route;
      }
    
      toJSON(): RouteDTOJson {
        return {
          id: this._id,
          name: this._name,
          distance: this._distance,
          start: this._start,
          url: this._url,
          info: this._info
        };
      }
}