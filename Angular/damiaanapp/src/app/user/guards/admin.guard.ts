import { AuthenticationService } from "../authentication.service";
import { Injectable } from "@angular/core";
import {
  CanActivate,
  ActivatedRouteSnapshot,
  RouterStateSnapshot,
  Router,
  UrlTree,
} from "@angular/router";
import { Observable } from 'rxjs';

@Injectable({
  providedIn: "root",
})
export class AdminGuard implements CanActivate {    
    role: string;    
    constructor(private _router: Router, private _authService: AuthenticationService) {    
        this._authService.role$.subscribe(r => {    
            this.role = r;    
      });    
    }    
      
    canActivate(    
      next: ActivatedRouteSnapshot,    
      state: RouterStateSnapshot): Observable<boolean | UrlTree> | Promise<boolean | UrlTree> | boolean | UrlTree {    
      
      if (this.role === "admin") {    
        return true;    
      }    
      
      this._router.navigate(['/not-found']);    
      return false;    
    }    
  }   