import {Injectable} from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {News} from '../dtos/news';
import {Observable} from 'rxjs';
import {Globals} from '../global/globals';

@Injectable({
  providedIn: 'root'
})
export class ApplicationNewsService {

  private newsBaseUri: string = this.globals.backendUri + '/news';

  constructor(private httpClient: HttpClient, private globals: Globals) {
  }

  /**
   * Persists message to the backend
   *
   * @param news to persist
   */
  publishNews(news: News): Observable<News> {
    console.log('Create news with title ' + news.title);
    return this.httpClient.post<News>(this.newsBaseUri, news);
  }
}
