import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { BehaviorSubject, Observable } from 'rxjs';
import { map, catchError } from 'rxjs/operators';
import { environment } from 'src/environments/environment';

@Injectable()
export class AuthenticationService {
  private readonly _tokenKey = 'currentUser';
  private _user$: BehaviorSubject<string>;
  private _role$: BehaviorSubject<string>;
  redirectUrl: string = undefined;
  
  constructor(private http: HttpClient) {
    let parsedToken = parseJwt(localStorage.getItem(this._tokenKey));
    if (parsedToken) {
      const expires =
        new Date(parseInt(parsedToken.exp, 10) * 1000) < new Date();
      if (expires) {
        localStorage.removeItem(this._tokenKey);
        parsedToken = null;
        this._user$ = new BehaviorSubject<string>(null);
        this._role$ = new BehaviorSubject<string>(null);
      } else {
        this._user$ = new BehaviorSubject<string>(
          parsedToken && parsedToken.unique_name
        );
        this._role$ = new BehaviorSubject<string>(parsedToken.role);
      }
    } else {
      this._user$ = new BehaviorSubject<string>(null);
      this._role$ = new BehaviorSubject<string>(null);
    }

  }

  login(email: string, password: string): Observable<boolean> {
    return this.http
      .post(
        `${environment.apiUrl}/account`,
        { email, password },
        { responseType: 'text' }
      )
      .pipe(
        map((json: any) => {
          if (json) {
            localStorage.setItem(this._tokenKey, JSON.parse(json).token);
            localStorage.setItem('userId', JSON.parse(json).id);
            this._user$.next(email);
            const decodeUserDetails = JSON.parse(window.atob(localStorage.getItem('currentUser').split('.')[1]));
            this._role$.next(decodeUserDetails.role);
            return true;
          } else {
            return false;
          }
        })
      );
  }

  register(
    email: string,
    password: string,
    firstname: string,
    name: string,
    passwordConfirmation: string,
    birthDate: Date
  ): Observable<boolean> {
    return this.http
      .post(
        `${environment.apiUrl}/Account/Register`,
        {
          email,
          password,
          firstname,
          name,
          passwordConfirmation,
          birthDate,
        },
        { responseType: 'text' }
      )
      .pipe(
        map((json: any) => {
          if (json) {
            localStorage.setItem(this._tokenKey, JSON.parse(json).token);
            localStorage.setItem('userId', JSON.parse(json).id);
            this._user$.next(email);
            return true;
          } else {
            return false;
          }
        })
      );
  }

  logout() {
    if (this._user$.getValue()) {
      localStorage.removeItem('currentUser');
      this._user$.next(null);
      this._role$.next(null);
    }
  }
  checkUserNameAvailability = (email: string): Observable<boolean> => {
    return this.http.get<boolean>(
      `${environment.apiUrl}/account/checkusername`,
      {
        params: { email },
      }
    );
  };
  
  get user$(): BehaviorSubject<string> {
    return this._user$;
  }

  get role$(): BehaviorSubject<string> {
    return this._role$;
  }

  get token(): string {
    const localToken = localStorage.getItem(this._tokenKey);
    return !!localToken ? localToken : "";
  }
}

function parseJwt(token) {
  if (!token) {
    return null;
  }
  const base64Token = token.split('.')[1];
  const base64 = base64Token.replace(/-/g, '+').replace(/_/g, '/');
  return JSON.parse(window.atob(base64));
}
