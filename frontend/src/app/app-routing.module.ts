import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { ArticleComponent } from './components/article/article.component';
import { EventDetailComponent } from './components/event-detail/event-detail.component';
import { EventsComponent } from './components/events/events.component';
import { HomePageComponent } from './components/home-page/home-page.component';
import { LoginComponent } from './components/login/login.component';
import { NewsComponent } from './components/news/news.component';
import { RegisterComponent } from './components/register/register.component';
import { ShoppingcartTestingComponent } from './components/shoppingcart-testing/shoppingcart-testing.component';
import { TicketDetailComponent } from './components/ticket-detail/ticket-detail.component';
import { UserHomeComponent } from './components/user-home/user-home.component';

const routes: Routes = [
  { path: '', component: HomePageComponent },
  { path: 'login', component: LoginComponent },
  { path: 'register', component: RegisterComponent },
  { path: 'news', component: NewsComponent },
  { path: 'events', component: EventsComponent },
  { path: 'article', component: ArticleComponent },
  { path: 'event', component: EventDetailComponent },
  { path: 'user', component: UserHomeComponent },
  { path: 'ticket', component: TicketDetailComponent },
  { path: 'sctest', component: ShoppingcartTestingComponent}
];

@NgModule({
  imports: [
    RouterModule.forRoot(routes, { scrollPositionRestoration: 'enabled' }),
  ],
  exports: [RouterModule],
})
export class AppRoutingModule {}
