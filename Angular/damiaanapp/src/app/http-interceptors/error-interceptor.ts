 import {Injectable} from "@angular/core";
import {HttpEvent, HttpHandler, HttpInterceptor,HttpRequest,HttpResponse,HttpErrorResponse} from '@angular/common/http';
import {Observable, of, throwError} from "rxjs";
import {catchError, map} from 'rxjs/operators';
import { Router } from '@angular/router';
import { httpInterceptorProviders } from '.';
 
@Injectable()
export class ErrorInterceptor implements HttpInterceptor {
    
  constructor(public router: Router) {
  }
 
  intercept(req: HttpRequest<any>, next: HttpHandler): Observable<HttpEvent<any>> { 
    return next.handle(req).pipe(
      catchError((error) => {
        if(error instanceof HttpErrorResponse) {
            if(error.status == 500) {
                this.router.navigateByUrl('server-error')
            }
        }
        return throwError(error);
      })
    )
  }
}