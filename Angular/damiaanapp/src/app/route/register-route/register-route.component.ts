import { Input } from '@angular/core';
import { Component, OnInit } from '@angular/core';
import {
  FormBuilder,
  FormGroup,
  Validators,
  NgForm,
  FormControl,
} from '@angular/forms';
import { MatSnackBar } from '@angular/material/snack-bar';
import { ActivatedRoute, Router } from '@angular/router';
import { EMPTY, Observable } from 'rxjs';
import { catchError } from 'rxjs/operators';
import { AuthenticationService } from 'src/app/user/authentication.service';
import { Registration } from '../registration.model';
import { Route } from '../route.model';
import { RouteService } from '../route.service';

@Component({
  selector: 'app-register-route',
  templateUrl: './register-route.component.html',
})
export class RegisterRouteComponent implements OnInit {
  @Input() routeId: number;
  route$: Observable<Route>;
  loggedInUser$ = this._authenticationService.user$;
  errorMessage: string = '';
  registration: FormGroup;
  hideSizes: boolean = true;
  public isLoading: boolean = false;

  constructor(
    private _routeService: RouteService,
    private _activatedRoute: ActivatedRoute,
    private _authenticationService: AuthenticationService,
    private _snackbar: MatSnackBar,
    private _fb: FormBuilder,
    private _router: Router
  ) {}

  ngOnInit(): void {
    this._activatedRoute.paramMap.subscribe(
      (p) => (this.routeId = +p.get('id'))
    );
    this.route$ = this._routeService.getRouteById(this.routeId).pipe(
      catchError((err) => {
        this.errorMessage = err;
        this.opensnackbar();
        return EMPTY;
      })
    );
    this.registration = this._fb.group({
      shirtBool: false,
      shirtSize: [null],
    });
    if (this.loggedInUser$.value == null) {
      this._authenticationService.redirectUrl = `/${this.routeId}/register`
    }
  }

  onSubmit() {
    this.isLoading = true;
    this._routeService.addNewRegistration(
      this.routeId,
      new Registration(
        0,
        false,
        this.registration.get('shirtBool').value,
        this.registration.get('shirtSize').value
      )
    ).subscribe(() => {
      this.isLoading = false;
      this._router.navigate(['/my-routes'])
    });
  }

  opensnackbar() {
    this._snackbar.open('Something went wrong!', '', {
      duration: 5000,
      panelClass: ['red-snackbar'],
    });
  }

  changeVisibility(visible: boolean) {
    this.hideSizes = !visible;
  }
}
