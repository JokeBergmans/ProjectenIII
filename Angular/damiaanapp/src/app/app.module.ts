import { BrowserModule } from '@angular/platform-browser';
import { HttpClientModule } from '@angular/common/http';
import { NgModule } from '@angular/core';
import { AgmCoreModule } from '@agm/core';
import { AppComponent } from './app.component';
import { AddRouteComponent } from './route/add-route/add-route.component';
import { MaterialModule } from './material/material.module';
import { WelcomeComponent } from './welcome/welcome.component';
import { RoutingModule } from './routing.module';
import { HeaderComponent } from './navigation/header/header.component';
import { RouteModule } from './route/route.module';
import { UserModule } from './user/user.module';
import { InputDialogComponent } from './input-dialog/input-dialog.component';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { MatDialogModule } from '@angular/material/dialog';
import { AuthenticationService } from './user/authentication.service';
import { LOCALE_ID } from '@angular/core';
import { registerLocaleData } from '@angular/common';
import localeNl from '@angular/common/locales/Nl';
import { NgbModule } from '@ng-bootstrap/ng-bootstrap';
import { MatInputModule } from '@angular/material/input';
import { PickUserComponent } from './follow/pick-user/pick-user.component';
import { FollowMapComponent } from './follow/follow-map/follow-map.component';
import { httpInterceptorProviders } from './http-interceptors';
import { NotFoundComponent } from './not-found/not-found.component';
import { ParticipantModule } from './participant/participant.module';
import { ErrorInterceptor } from './http-interceptors/error-interceptor';
import { HTTP_INTERCEPTORS } from "@angular/common/http";


 registerLocaleData(localeNl, 'nl');

@NgModule({
  declarations: [
    AppComponent,
    WelcomeComponent,
    HeaderComponent,
    InputDialogComponent,
    PickUserComponent,
    FollowMapComponent,
    NotFoundComponent
  ],
  imports: [
    BrowserModule,
    HttpClientModule,
    MaterialModule,
    RoutingModule,
    RouteModule,
    UserModule,
    FormsModule,
    ParticipantModule,
    ReactiveFormsModule,
    MatDialogModule,
    MatInputModule,
    NgbModule,
    AgmCoreModule.forRoot({
      apiKey: 'AIzaSyBEKczwfvRT8-67v0oAgHS3N_5aNuF2CMQ',
    })
  ],
  providers: [AuthenticationService,{ provide: HTTP_INTERCEPTORS, useClass: ErrorInterceptor, multi: true  } ,{ provide: LOCALE_ID, useValue: 'Nl' }, httpInterceptorProviders],
  bootstrap: [AppComponent],
})
export class AppModule {}
