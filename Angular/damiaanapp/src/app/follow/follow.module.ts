import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { PickUserComponent } from './pick-user/pick-user.component';
import { FollowMapComponent } from './follow-map/follow-map.component';
import { MaterialModule } from '../material/material.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';





@NgModule({
  declarations: [PickUserComponent, FollowMapComponent],
  imports: [
    CommonModule,
    MaterialModule,
    ReactiveFormsModule,
    FormsModule
  ]
})
export class FollowModule { }
