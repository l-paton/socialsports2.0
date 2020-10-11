import { NgModule } from '@angular/core';
import { Routes, RouterModule, CanActivate, ActivatedRouteSnapshot, RouterStateSnapshot } from '@angular/router';

import { LoginComponent } from './components/login/login.component';
import { SignupComponent } from './components/signup/signup.component';
import { HomeComponent } from './components/home/home.component';
import { EventsComponent } from './components/events/events.component';
import { AuthGuard } from './_helpers/auth.guard';
import { HideGuard } from './_helpers/hide.guard';

const routes: Routes = [
  { path: 'login', component: LoginComponent, canDeactivate: [HideGuard]},
  { path: 'signup', component: SignupComponent, canDeactivate: [HideGuard]},
  { path: 'home', component: HomeComponent, canActivate: [AuthGuard]},
  { path: 'event', component: EventsComponent, canActivate: [AuthGuard] },
  { path: '', component: LoginComponent, canDeactivate: [HideGuard]},
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
