import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';
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
import { ArticleComponent } from './components/article/article.component';
import { EventsComponent } from './components/events/events.component';
import { EventListItemComponent } from './components/event-list-item/event-list-item.component';
import { SearchComponent } from './components/search/search.component';
import { UserHomeComponent } from './components/user-home/user-home.component';
import { TicketDetailComponent } from './components/ticket-detail/ticket-detail.component';
import { TicketListItemComponent } from './components/ticket-list-item/ticket-list-item.component';
import { ShoppingcartTestingComponent } from './components/shoppingcart-testing/shoppingcart-testing.component';
import { ShoppingcartComponent } from './components/shoppingcart/shoppingcart.component';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { HttpClientModule } from '@angular/common/http';
import { AddUserComponent } from './components/add-user/add-user.component';

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
    ShoppingcartTestingComponent,
    ShoppingcartComponent,
    AddUserComponent
  ],
  imports: [
    BrowserModule,
    AppRoutingModule,
    ReactiveFormsModule,
    HttpClientModule,
    FormsModule,
  ],
  providers: [],
  bootstrap: [AppComponent],
})
export class AppModule {}
