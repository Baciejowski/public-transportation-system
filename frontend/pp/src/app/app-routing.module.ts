import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { AuthenticatedGuard } from './features/auth/guards/authenticated.guard';
import { LoginComponent } from './features/auth/pages/login/login.component';
import { RegisterComponent } from './features/auth/pages/register/register.component';
import { LineComponent } from './features/schedule/pages/line/line.component';
import { SchedulesComponent } from './features/schedule/pages/schedules/schedules.component';
import { TicketOfferComponent } from './features/tickets/pages/ticket-offer/ticket-offer.component';

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
    path: "tickets/offer",
    component: TicketOfferComponent,
    canActivate: [AuthenticatedGuard]
  },
  {
    path: "schedules",
    component: SchedulesComponent,
    canActivate: [AuthenticatedGuard]
  },
  {
    path: "lines/:lineId",
    component: LineComponent,
    canActivate: [AuthenticatedGuard]
  },
  {
    path: "**",
    redirectTo: "schedules"
  }
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
