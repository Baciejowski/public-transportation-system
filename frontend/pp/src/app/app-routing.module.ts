import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { AuthenticatedGuard } from './features/auth/guards/authenticated.guard';
import { LoginComponent } from './features/auth/pages/login/login.component';
import { RegisterComponent } from './features/auth/pages/register/register.component';
import { DeviationsComponent } from './features/schedule/pages/deviations/deviations.component';
import { LineComponent } from './features/schedule/pages/line/line.component';
import { SchedulesComponent } from './features/schedule/pages/schedules/schedules.component';
import { StopComponent } from './features/schedule/pages/stop/stop.component';
import { ActiveTicketComponent } from './features/tickets/pages/active-ticket/active-ticket.component';
import { ActiveTicketsComponent } from './features/tickets/pages/active-tickets/active-tickets.component';
import { BuyTicketSuccessComponent } from './features/tickets/pages/buy-ticket-success/buy-ticket-success.component';
import { BuyTicketComponent } from './features/tickets/pages/buy-ticket/buy-ticket.component';
import { OwnedTicketsComponent } from './features/tickets/pages/owned-tickets/owned-tickets.component';
import { PunchTicketSuccessComponent } from './features/tickets/pages/punch-ticket-success/punch-ticket-success.component';
import { PunchTicketComponent } from './features/tickets/pages/punch-ticket/punch-ticket.component';
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
    canActivate: [AuthenticatedGuard],
    data: {
      roles: ['PASSENGER']
    }
  },
  {
    path: "tickets/owned",
    component: OwnedTicketsComponent,
    canActivate: [AuthenticatedGuard],
    data: {
      roles: ['PASSENGER']
    }
  },
  {
    path: "tickets/buy",
    component: BuyTicketComponent,
    canActivate: [AuthenticatedGuard],
    data: {
      roles: ['PASSENGER']
    }
  },
  {
    path: "tickets/buy/success",
    component: BuyTicketSuccessComponent,
    canActivate: [AuthenticatedGuard],
    data: {
      roles: ['PASSENGER']
    }
  },
  {
    path: "tickets/punch",
    component: PunchTicketComponent,
    canActivate: [AuthenticatedGuard],
    data: {
      roles: ['PASSENGER']
    }
  },
  {
    path: "tickets/punch/success",
    component: PunchTicketSuccessComponent,
    canActivate: [AuthenticatedGuard],
    data: {
      roles: ['PASSENGER']
    }
  },
  {
    path: "tickets/active",
    component: ActiveTicketsComponent,
    canActivate: [AuthenticatedGuard],
    data: {
      roles: ['PASSENGER']
    }
  },
  {
    path: "tickets/active/info",
    component: ActiveTicketComponent,
    canActivate: [AuthenticatedGuard],
    data: {
      roles: ['PASSENGER']
    }
  },
  {
    path: "schedules",
    component: SchedulesComponent,
    canActivate: [AuthenticatedGuard],
    data: {
      roles: ['PASSENGER', 'PLANNER']
    }
  },
  {
    path: "lines/:lineId",
    component: LineComponent,
    canActivate: [AuthenticatedGuard],
    data: {
      roles: ['PASSENGER', 'PLANNER']
    }
  },
  {
    path: "stops/:stopId",
    component: StopComponent,
    canActivate: [AuthenticatedGuard],
    data: {
      roles: ['PASSENGER', 'PLANNER']
    }
  },
  {
    path: "deviations",
    component: DeviationsComponent,
    canActivate: [AuthenticatedGuard],
    data: {
      roles: ['PASSENGER', 'PLANNER']
    }
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
