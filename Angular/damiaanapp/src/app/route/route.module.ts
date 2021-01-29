import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { MaterialModule } from '../material/material.module';
import { AgmCoreModule } from '@agm/core';
import { AddRouteComponent } from './add-route/add-route.component';
import { AddFileComponent } from './add-file/add-file.component';
import { MyRoutesComponent } from './my-routes/my-routes.component';
import { RouterModule } from '@angular/router';
import { RegisterRouteComponent } from './register-route/register-route.component';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatSnackBarModule } from '@angular/material/snack-bar';
import { ReactiveFormsModule } from '@angular/forms';
import { QrcodesComponent } from './qrcodes/qrcodes.component';
import { NgxQRCodeModule } from '@techiediaries/ngx-qrcode';
import { RouteListComponent } from './route-list/route-list.component';
import { RouteDetailComponent } from './route-detail/route-detail.component';


@NgModule({
  declarations: [
    AddRouteComponent,
    AddFileComponent,
    MyRoutesComponent,
    RegisterRouteComponent,
    QrcodesComponent,
    RouteListComponent,
    RouteDetailComponent,
  ],
  imports: [
    CommonModule,
    MaterialModule,
    ReactiveFormsModule,
    RouterModule,
    NgxQRCodeModule,
    AgmCoreModule.forRoot({
      apiKey: 'AIzaSyBEKczwfvRT8-67v0oAgHS3N_5aNuF2CMQ',
    })
  ],
  exports: [AddRouteComponent],
})
export class RouteModule {}
