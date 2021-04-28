import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';

import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { HomePageComponent } from './home-page/home-page.component';
import { NavbarComponent } from './navbar/navbar.component';
import { FooterComponent } from './footer/footer.component';
import { LoginComponent } from './login/login.component';
import { RegisterComponent } from './register/register.component';
import { EventDetailComponent } from './event-detail/event-detail.component';
import { NewsComponent } from './news/news.component';
import { NewsItemComponent } from './news-item/news-item.component';
import { ArticleComponent } from './article/article.component';
import { EventsComponent } from './events/events.component';
import { EventListItemComponent } from './event-list-item/event-list-item.component';
import { SearchComponent } from './search/search.component';
import { UserHomeComponent } from './user-home/user-home.component';
import { TicketDetailComponent } from './ticket-detail/ticket-detail.component';
import { TicketListItemComponent } from './ticket-list-item/ticket-list-item.component';

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
    ArticleComponent,
    EventsComponent,
    EventListItemComponent,
    SearchComponent,
    UserHomeComponent,
    TicketDetailComponent,
    TicketListItemComponent,
  ],
  imports: [BrowserModule, AppRoutingModule],
  providers: [],
  bootstrap: [AppComponent],
})
export class AppModule {}
