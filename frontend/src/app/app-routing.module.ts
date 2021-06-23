import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { EventDetailComponent } from './components/event-detail/event-detail.component';
import { SearchResultComponent } from './components/search/search-result/search-result.component';
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
import { AddEventComponent } from './components/event/add-event/add-event.component';
import { PerformanceDetailComponent } from './components/performance-detail/performance-detail.component';
import { BookingComponent } from './components/booking/booking.component';
import { AddressDetailComponent } from './components/search/address-detail/address-detail.component';
import { ArtistDetailComponent } from './components/search/artist-detail/artist-detail.component';
import { AddVenueComponent } from './components/venue/add-venue/add-venue.component';
import {EditUserComponent} from './components/edit-user/edit-user.component';
import {UserManagementComponent} from './components/user-management/user-management.component';
import {BannedAlertComponent} from './components/banned-alert/banned-alert.component';
import {StatusGuard} from './guards/status.guard';
import {MasterGuard} from './guards/master.guard';
import { ExploreComponent } from './components/explore/explore.component';

const routes: Routes = [
  { path: '', component: HomePageComponent, canActivate: [MasterGuard],
      data: {guards: [StatusGuard]} },
  { path: 'login', component: LoginComponent, canActivate: [MasterGuard],
      data: {guards: [StatusGuard]} },
  { path: 'register', component: RegisterComponent, canActivate: [MasterGuard],
      data: {guards: [StatusGuard]} },
  { path: 'news', component: NewsComponent, canActivate: [MasterGuard],
      data: {guards: [StatusGuard]} },
  { path: 'events', component: SearchResultComponent, canActivate: [MasterGuard],
      data: {guards: [StatusGuard]} },
  { path: 'news-detail/:id', component: NewsDetailComponent, canActivate: [MasterGuard],
      data: {guards: [StatusGuard]} },
  { path: 'address-detail/:id', component: AddressDetailComponent, canActivate: [MasterGuard],
      data: {guards: [StatusGuard]} },
  { path: 'artist-detail/:id', component: ArtistDetailComponent, canActivate: [MasterGuard],
      data: {guards: [StatusGuard]} },
  { path: 'event-detail/:id', component: EventDetailComponent, canActivate: [MasterGuard],
      data: {guards: [StatusGuard]} },
  { path: 'add-venue', component: AddVenueComponent, canActivate: [MasterGuard],
      data: {guards: [AuthGuard, StatusGuard], roles: ['ADMIN', 'ORGANIZER'] } },
  { path: 'performance-detail/:id', component: PerformanceDetailComponent, canActivate: [MasterGuard],
      data: {guards: [StatusGuard]} },
  { path: 'user', component: UserHomeComponent, canActivate: [MasterGuard],
      data: { guards: [AuthGuard, StatusGuard], roles: ['ADMIN', 'ORGANIZER', 'USER']} },
  { path: 'ticket', component: TicketDetailComponent, canActivate: [MasterGuard],
      data: {guards: [AuthGuard, StatusGuard], roles: ['ADMIN', 'ORGANIZER', 'USER']} },
  { path: 'add-news/:id', component: AddNewsComponent, canActivate: [MasterGuard],
      data: {guards: [AuthGuard, StatusGuard], roles: ['ADMIN', 'ORGANIZER']} },
  { path: 'add-user', component: AddUserComponent, canActivate: [MasterGuard],
      data: {guards: [AuthGuard, StatusGuard], roles: ['ADMIN']} },
  { path: 'add-event', component: AddEventComponent, canActivate: [MasterGuard],
      data: {guards: [AuthGuard, StatusGuard], roles: ['ADMIN', 'ORGANIZER']} },
  { path: 'orders', component: BookingComponent, canActivate: [MasterGuard],
      data: {guards: [AuthGuard, StatusGuard], roles: ['ADMIN', 'ORGANIZER', 'USER']} },
  { path: 'edit-user/:id', component: EditUserComponent, canActivate: [MasterGuard],
      data: {guards: [AuthGuard, StatusGuard], roles: ['ADMIN', 'ORGANIZER', 'USER']} },
  { path: 'users', component: UserManagementComponent, canActivate: [MasterGuard],
      data: {guards: [AuthGuard, StatusGuard], roles: ['ADMIN']} },
  { path: 'banned', component: BannedAlertComponent }
];

@NgModule({
  imports: [
    RouterModule.forRoot(routes, { scrollPositionRestoration: 'enabled' }),
  ],
  exports: [RouterModule],
})
export class AppRoutingModule {}
