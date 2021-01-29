import { Component, OnInit, Output, EventEmitter } from '@angular/core';
import { AuthenticationService } from 'src/app/user/authentication.service';

@Component({
  selector: 'app-header',
  templateUrl: './header.component.html',
  providers : []
})
export class HeaderComponent implements OnInit {

  loggedInUser$ = this._authenticationService.user$;
  role: string;    

  @Output() public sidenavToggle = new EventEmitter();

  constructor(private _authenticationService: AuthenticationService) {    
      this._authenticationService.role$.subscribe(r => { 
        this.role = r;   
    });    
  }  

  ngOnInit(): void {
    
  }
  logout() : void {
    this._authenticationService.logout()
  }

  public onToggleSidenav = () => { 
    this.sidenavToggle.emit();
  }

}
