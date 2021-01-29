import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { Routes, RouterModule } from '@angular/router';
import { WelcomeComponent } from './welcome/welcome.component';
import { AddRouteComponent } from './route/add-route/add-route.component';
import { RegisterComponent } from './user/register/register.component';
import { LoginComponent } from './user/login/login.component';
import { MyRoutesComponent } from './route/my-routes/my-routes.component';
import { RegisterRouteComponent } from './route/register-route/register-route.component';
import { PickUserComponent } from './follow/pick-user/pick-user.component';
import { FollowMapComponent } from './follow/follow-map/follow-map.component';
import { QrcodesComponent } from './route/qrcodes/qrcodes.component';
import { RouteListComponent } from './route/route-list/route-list.component';
import { ParticipantListComponent } from './participant/participant-list/participant-list.component';
import { AuthGuard } from './user/guards/auth.guard';
import { AdminGuard } from './user/guards/admin.guard';
import { NotFoundComponent } from './not-found/not-found.component';
import { ServerErrorComponent } from './server-error/server-error.component';
import { RouteDetailComponent } from './route/route-detail/route-detail.component';


const routes: Routes = [
  { path : 'home', component: WelcomeComponent},
  { path : 'addRoute', canActivate: [AuthGuard, AdminGuard], component: AddRouteComponent},
  { path : 'register', component: RegisterComponent},
  { path : 'login', component : LoginComponent},
  { path : 'my-routes', canActivate: [AuthGuard], component : MyRoutesComponent},
  { path : 'route/:id/detail', component: RouteDetailComponent},
  { path : ':id/qrcodes', canActivate: [AuthGuard, AdminGuard], component: QrcodesComponent},
  { path : 'pick-user/:routeId', canActivate: [AuthGuard], component : PickUserComponent},
  { path : 'follow-map/:routeid/:registrationid', canActivate: [AuthGuard], component : FollowMapComponent},
  { path : 'follow-map/:routeId', canActivate: [AuthGuard], component: PickUserComponent},
  { path : 'routes/list', canActivate: [AuthGuard, AdminGuard], component: RouteListComponent},
  { path : ':id/register', component: RegisterRouteComponent},
  { path : 'participants/list', canActivate: [AuthGuard, AdminGuard], component: ParticipantListComponent},
  { path : 'not-found', component: NotFoundComponent},
  { path : 'server-error', component : ServerErrorComponent},
  { path : '', redirectTo: '/home', pathMatch: 'full' },
  { path : '**', redirectTo: '/not-found' }
];

@NgModule({
  declarations: [],
  imports: [
    CommonModule,
    RouterModule.forRoot(routes)
  ],
  exports: [
    RouterModule
  ]
})
export class RoutingModule { }
