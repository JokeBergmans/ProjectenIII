import { HttpClient, HttpErrorResponse, HttpHandler } from '@angular/common/http';
import { Component, OnInit } from '@angular/core';
import { AbstractControl, FormBuilder, ReactiveFormsModule, FormGroup, ValidationErrors, ValidatorFn, Validators } from '@angular/forms';
import { MatDatepickerModule } from '@angular/material/datepicker';
import { Router } from '@angular/router';
import { Observable } from 'rxjs/internal/Observable';
import { map } from 'rxjs/operators';
import { AuthenticationService } from '../authentication.service';



@Component({
  selector: 'app-register',
  templateUrl: './register.component.html',
  providers: [HttpClient, MatDatepickerModule]
})
export class RegisterComponent implements OnInit {
  public user: FormGroup;
  public isLoading: Boolean = false;
  public errorMessage : string

  constructor(
    private authService: AuthenticationService,
    private router: Router,
    private fb: FormBuilder
  ) { }
  ngOnInit() {
    this.user = this.fb.group({
      firstname: ['', Validators.required],
      lastname: ['', Validators.required],
      email: [
        '', [Validators.required, Validators.email], serverSideValidateUsername(this.authService.checkUserNameAvailability)
      ],
      passwordGroup: this.fb.group({
        password: ['', [Validators.required, Validators.minLength(8), passwordValidator]],
        confirmPassword: ['', Validators.required]
      }, { validator: comparePasswords }),
      birthDate: ['', [Validators.required, dateValidator] ]
    });
  }
  onSubmit() {
    this.isLoading = true;
    this.authService.register(this.user.get('email').value, this.user.get('passwordGroup.password').value, this.user.get('firstname').value, this.user.get('lastname').value, this.user.get('passwordGroup.confirmPassword').value, this.user.get('birthDate').value).subscribe(val => {
      if (val) {
        this.isLoading = false;
        if (this.authService.redirectUrl) {
          this.router.navigateByUrl(this.authService.redirectUrl);
          this.authService.redirectUrl = undefined;
        } else {
          this.router.navigate(['/home'])
        }
      } else {
        this.isLoading = false;
        this.errorMessage = 'Er liep iets mis bij het registreren. Probeer het later opnieuw.'
      }
    },
      (err: HttpErrorResponse) => {
        console.log(err.error);
      }
    )

  }

  getErrorMessage(errors: any) {
    if (!errors) {
      return null;
    }
    if (errors.required) {
      return "is verplicht";
    } else if (errors.minlength) {
      return `moet minstens ${errors.minlength.requiredLength} tekens lang zijn (is nu ${errors.minlength.actualLength})`;
    } else if (errors.regexFail) {
      return `moet minstens 1 hoofdletter, cijfer en symbool bevatten`;
    } else if (errors.userAlreadyExists) {
      return `dit e-mailadres is reeds geregistreerd`;
    } else if (errors.email) {
      return `voer een geldig e-mailadres in`;
    } else if (errors.passwordsDiffer) {
      return `wachtwoorden komen niet overeen`;
    } else if (errors.ageBelow18) {
      return `u moet minstens 18 zijn om een account aan te maken`;
    } 
  }
}

function comparePasswords(control: AbstractControl): ValidationErrors {
  const password = control.get('password');
  const confirmPassword = control.get('confirmPassword');
  return password.value === confirmPassword.value ? null
    : { 'passwordsDiffer': true };
}

function serverSideValidateUsername(
  checkAvailabilityFn: (n: string) => Observable<boolean>
): ValidatorFn {
  return (control: AbstractControl): Observable<ValidationErrors> => {
    return checkAvailabilityFn(control.value).pipe(
      map(available => {
        if (available) {
          return null;
        }
        return { 'userAlreadyExists': true };
      })
    );
  };
}

function passwordValidator(control : AbstractControl) : ValidationErrors {
  return /(?=.*\d)(?=.*[a-z])(?=.*[A-Z])(?=.*\W)/.test(control.value) ? null : { 'regexFail' : true}
}

function dateValidator(control: AbstractControl): ValidationErrors {
  let date = new Date(control.value);
  let timeDiff = Math.abs(Date.now() - date.getTime());
  let age = Math.floor((timeDiff / (1000 * 3600 * 24))/365.25);
  return age >= 18 ? null
    : { 'ageBelow18': true };
  }


