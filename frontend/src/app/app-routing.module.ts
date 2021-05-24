import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { EventDetailComponent } from './components/event-detail/event-detail.component';
import { EventsComponent } from './components/events/events.component';
import { HomePageComponent } from './components/home-page/home-page.component';
import { LoginComponent } from './components/login/login.component';
import { NewsComponent } from './components/news/news.component';
import { RegisterComponent } from './components/register/register.component';
import { TicketDetailComponent } from './components/ticket-detail/ticket-detail.component';
import { UserHomeComponent } from './components/user-home/user-home.component';
import { AddNewsComponent } from './components/add-news/add-news.component';
import { NewsDetailComponent } from './components/news-detail/news-details.component';
import { AddUserComponent } from './components/add-user/add-user.component';
import { AuthGuard } from './guards/auth.guard';
import { AddVenueComponent } from './components/venue/add-venue/add-venue.component';
import { AddEventComponent } from './components/event/add-event/add-event.component';

const routes: Routes = [
  { path: '', component: HomePageComponent },
  { path: 'login', component: LoginComponent },
  { path: 'register', component: RegisterComponent },
  { path: 'news', component: NewsComponent },
  { path: 'events', component: EventsComponent },
  { path: 'news-detail/:id', component: NewsDetailComponent },
  { path: 'event-detail/:id', component: EventDetailComponent },
  {
    path: 'add-venue',
    component: AddVenueComponent,
    canActivate: [AuthGuard],
    data: { roles: ['ADMIN', 'ORGANIZER'] },
  },
  { path: 'user', component: UserHomeComponent, canActivate: [AuthGuard], data: { roles: ['ADMIN', 'ORGANIZER', 'USER']} },
  { path: 'ticket', component: TicketDetailComponent, canActivate: [AuthGuard], data: { roles: ['ADMIN', 'ORGANIZER', 'USER']} },
  { path: 'add-news/:id', component: AddNewsComponent, canActivate: [AuthGuard], data: { roles: ['ADMIN', 'ORGANIZER']} },
  { path: 'add-user', component: AddUserComponent, canActivate: [AuthGuard], data: { roles: ['ADMIN']} },
  { path: 'add-event', component: AddEventComponent, canActivate: [AuthGuard], data: { roles: ['ADMIN', 'ORGANIZER']} }
];

@NgModule({
  imports: [
    RouterModule.forRoot(routes, { scrollPositionRestoration: 'enabled' }),
  ],
  exports: [RouterModule],
})
export class AppRoutingModule {}
