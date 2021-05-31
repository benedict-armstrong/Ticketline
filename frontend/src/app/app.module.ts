import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { HttpClientModule } from '@angular/common/http';

import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { HomePageComponent } from './components/home-page/home-page.component';
import { NavbarComponent } from './components/navbar/navbar.component';
import { FooterComponent } from './components/footer/footer.component';
import { LoginComponent } from './components/login/login.component';
import { RegisterComponent } from './components/register/register.component';
import { EventDetailComponent } from './components/event-detail/event-detail.component';
import { NewsComponent } from './components/news/news.component';
import { NewsItemComponent } from './components/news-item/news-item.component';
import { NewsDetailComponent } from './components/news-detail/news-details.component';
import { SearchResultComponent } from './components/search/search-result/search-result.component';
import { EventListItemComponent } from './components/search/event-list-item/event-list-item.component';
import { EventSearchComponent } from './components/search/event-search/event-search.component';
import { UserHomeComponent } from './components/user-home/user-home.component';
import { TicketDetailComponent } from './components/ticket-detail/ticket-detail.component';
import { TicketListItemComponent } from './components/ticket-list-item/ticket-list-item.component';
import {AddNewsComponent} from './components/add-news/add-news.component';
import { AddPerformanceComponent } from './components/event/add-performance/add-performance.component';
import { AddUserComponent } from './components/add-user/add-user.component';
import { httpInterceptorProviders } from './interceptors';
import { AddSectortypeComponent } from './components/event/add-sectortype/add-sectortype.component';
import { AddArtistComponent } from './components/event/add-artist/add-artist.component';
import { AddAddressComponent } from './components/event/add-address/add-address.component';
import { AddEventComponent } from './components/event/add-event/add-event.component';
import { PerformanceDetailComponent } from './components/performance-detail/performance-detail.component';
import { ArtistSearchComponent } from './components/search/artist-search/artist-search.component';
import { AddressSearchComponent } from './components/search/address-search/address-search.component';
import { ArtistListItemComponent } from './components/search/artist-list-item/artist-list-item.component';
import { AddressListItemComponent } from './components/search/address-list-item/address-list-item.component';
import { PerformanceSearchComponent } from './components/search/performance-search/performance-search.component';
import { PerformanceListItemComponent } from './components/search/performance-list-item/performance-list-item.component';
import {LoadingAnimationComponent} from './components/loading-animation/loading-animation.component';
import { AddTickettypeComponent } from './components/event/add-tickettype/add-tickettype.component';
import { ArtistDetailComponent } from './components/search/artist-detail/artist-detail.component';
import { AddressDetailComponent } from './components/search/address-detail/address-detail.component';
import { SearchEventListComponent } from './components/search/search-event-list/search-event-list.component';

@NgModule({
  declarations: [
    AppComponent,
    HomePageComponent,
    NavbarComponent,
    FooterComponent,
    LoginComponent,
    RegisterComponent,
    EventDetailComponent,
    NewsComponent,
    NewsItemComponent,
    NewsDetailComponent,
    SearchResultComponent,
    EventListItemComponent,
    EventSearchComponent,
    UserHomeComponent,
    TicketDetailComponent,
    TicketListItemComponent,
    AddNewsComponent,
    AddPerformanceComponent,
    AddUserComponent,
    AddSectortypeComponent,
    AddArtistComponent,
    AddAddressComponent,
    AddEventComponent,
    PerformanceDetailComponent,
    ArtistSearchComponent,
    AddressSearchComponent,
    ArtistListItemComponent,
    AddressListItemComponent,
    PerformanceSearchComponent,
    PerformanceListItemComponent,
    LoadingAnimationComponent,
    AddTickettypeComponent,
    SearchEventListComponent,
    AddTickettypeComponent,
    ArtistDetailComponent,
    AddressDetailComponent
  ],
  imports: [
    BrowserModule,
    AppRoutingModule,
    ReactiveFormsModule,
    HttpClientModule,
    FormsModule,
  ],
  providers: [httpInterceptorProviders], // Interceptors defined in interceptors/index.ts
  bootstrap: [AppComponent],
})
export class AppModule {}
