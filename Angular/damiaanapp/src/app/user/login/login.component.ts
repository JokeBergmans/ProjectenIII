import { HttpErrorResponse } from '@angular/common/http';
import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import { HeaderComponent } from 'src/app/navigation/header/header.component';
import { AuthenticationService } from '../authentication.service';

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  providers : []
})
export class LoginComponent implements OnInit {
  public user : FormGroup
  public isLoading: boolean = false;
  public errorMessage : string

  constructor(
    private authService: AuthenticationService,
    private router: Router,
    private fb: FormBuilder
  ) { }

  ngOnInit(): void {
    this.user = this.fb.group({
      email: [
        '', [Validators.required, Validators.email]
        
      ],
      password : ['', Validators.required]
    });
  }
  
  onSubmit() {
    this.isLoading = true;
    this.authService.login(this.user.get('email').value,this.user.get('password').value).subscribe(val => {
      if (val) {
        this.isLoading = false;
        if (this.authService.redirectUrl) {
          this.router.navigateByUrl(this.authService.redirectUrl);
          this.authService.redirectUrl = undefined;
        } else {
          this.router.navigate(['/home'])

        }
      }
    },
      (err: HttpErrorResponse) => {
        this.errorMessage = "Je hebt een incorrecte e-mail of wachtwoord ingevoerd."
        this.isLoading = false;
      }
    )

  }

  getErrorMessage(errors: any) {
    if (!errors) {
      return null;
    }
    if (errors.required) {
      return "is verplicht";
    } else if (errors.email) {
      return `voer een geldig e-mailadres in`;
    } 
  }
}
