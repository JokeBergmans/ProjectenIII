import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';
import { RouteService } from 'src/app/route/route.service';

@Component({
  selector: 'app-pick-user',
  templateUrl: './pick-user.component.html',
})
export class PickUserComponent implements OnInit {

  public idGroup : FormGroup
  public routeId : number
  public registeredForRoute : number[]
  public errorMessage : string

  constructor(private fb : FormBuilder, private router : Router,private activeRoute : ActivatedRoute, private routeService : RouteService) { }

  ngOnInit(): void {
    this.activeRoute.params.subscribe(param => {
      this.routeId = param['routeId'];
    });
    this.idGroup = this.fb.group({
      followId : [
        '', [Validators.required]
        
      ]
    });
    this.routeService.getRegistrationsForRoute(this.routeId).subscribe(res => {
      console.log(res);     
      this.registeredForRoute = res;
    })
  }

  get f() { return this.idGroup.controls; }

  onSubmit() {
        if(this.registeredForRoute.indexOf(Number(this.idGroup.get('followId').value)) > -1) {       
          this.router.navigate(["/follow-map",this.routeId,this.idGroup.get('followId').value])  
        } else {
          if(this.idGroup.get('followId').valid) {
            this.errorMessage = 'Deze wandelaar is niet ingeschreven voor deze route'
          }
          
        }

  }

}
