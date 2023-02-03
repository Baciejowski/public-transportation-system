import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { AuthenticatedGuard } from './features/auth/guards/authenticated.guard';
import { LoginComponent } from './features/auth/pages/login/login.component';
import { RegisterComponent } from './features/auth/pages/register/register.component';
import { ValidationComponent } from './features/validate/pages/validation/validation.component';

const routes: Routes = [
  {
    path: "register",
    component: RegisterComponent
  },
  {
    path: "login",
    component: LoginComponent
  },
  {
    path: "validation",
    component: ValidationComponent,
    canActivate: [AuthenticatedGuard],
    data: {
      roles: ['TICKET_INSPECTOR']
    }
  },
  {
    path: "**",
    redirectTo: "validation"
  }
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
