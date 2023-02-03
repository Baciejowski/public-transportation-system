import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { LoginComponent } from './features/auth/pages/login/login.component';
import { RegisterComponent } from './features/auth/pages/register/register.component';
import { MatInputModule } from '@angular/material/input';
import { MatButtonModule } from '@angular/material/button';
import { MatSelectModule } from '@angular/material/select';
import { MatExpansionModule } from '@angular/material/expansion';
import { MatButtonToggleModule } from '@angular/material/button-toggle';
import { ReactiveFormsModule } from '@angular/forms';
import { HttpClient, HttpClientModule, HTTP_INTERCEPTORS } from '@angular/common/http';
import { SessionInterceptor } from './features/auth/interceptors/session.interceptor';
import { TranslateLoader, TranslateModule } from '@ngx-translate/core';
import { TranslateHttpLoader } from '@ngx-translate/http-loader';
import { LanguageSelectComponent } from './common/language-select/language-select.component';
import { TicketComponent } from './features/tickets/components/ticket/ticket.component';
import { TicketOfferComponent } from './features/tickets/pages/ticket-offer/ticket-offer.component';
import { SchedulesComponent } from './features/schedule/pages/schedules/schedules.component';
import { LineComponent } from './features/schedule/pages/line/line.component';
import { StopComponent } from './features/schedule/pages/stop/stop.component';
import { FormsModule } from '@angular/forms';
import { BuyTicketComponent } from './features/tickets/pages/buy-ticket/buy-ticket.component';
import { BuyTicketSuccessComponent } from './features/tickets/pages/buy-ticket-success/buy-ticket-success.component';
import { PunchTicketSuccessComponent } from './features/tickets/pages/punch-ticket-success/punch-ticket-success.component';
import { PunchTicketComponent } from './features/tickets/pages/punch-ticket/punch-ticket.component';
import { OwnedTicketsComponent } from './features/tickets/pages/owned-tickets/owned-tickets.component';
import { ActiveTicketsComponent } from './features/tickets/pages/active-tickets/active-tickets.component';
import { ActiveTicketComponent } from './features/tickets/pages/active-ticket/active-ticket.component';
import { DeviationsComponent } from './features/schedule/pages/deviations/deviations.component';
import { MenuComponent } from './common/menu/menu.component';
import { MatSidenavModule } from '@angular/material/sidenav';
import { FontAwesomeModule } from '@fortawesome/angular-fontawesome';
import { MatSnackBarModule } from '@angular/material/snack-bar';


export function HttpLoaderFactory(http: HttpClient) {
  return new TranslateHttpLoader(http);
}

const defaultLang = localStorage.getItem('lang') || 'en';

@NgModule({
  declarations: [
    AppComponent,
    RegisterComponent,
    LoginComponent,
    LanguageSelectComponent,
    TicketComponent,
    TicketOfferComponent,
    SchedulesComponent,
    LineComponent,
    StopComponent,
    BuyTicketComponent,
    BuyTicketSuccessComponent,
    PunchTicketSuccessComponent,
    PunchTicketComponent,
    OwnedTicketsComponent,
    ActiveTicketsComponent,
    ActiveTicketComponent,
    DeviationsComponent,
    MenuComponent
  ],
  imports: [
    BrowserModule,
    AppRoutingModule,
    ReactiveFormsModule,
    FormsModule,
    BrowserAnimationsModule,
    TranslateModule.forRoot({
      defaultLanguage: defaultLang,
      loader: {
        provide: TranslateLoader,
        useFactory: HttpLoaderFactory,
        deps: [HttpClient]
      }
    }),
    HttpClientModule,
    MatInputModule,
    MatButtonModule,
    MatSelectModule,
    MatExpansionModule,
    MatButtonToggleModule,
    MatSidenavModule,
    FontAwesomeModule,
    MatSnackBarModule
  ],
  providers: [
    { provide: HTTP_INTERCEPTORS, useClass: SessionInterceptor, multi: true },
  ],
  bootstrap: [AppComponent]
})
export class AppModule { }
