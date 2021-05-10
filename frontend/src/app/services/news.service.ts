import {Injectable} from '@angular/core';
import {HttpClient, HttpParams} from '@angular/common/http';
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

  /**
   * Returns an array of news
   *
   * @param limit the amount of news to be returned
   * @param offset the offset parameter for pagination
   */
  getNews(limit: number, offset: number): Observable<News[]> {
    console.log('Retrieve news (limit=' + limit + ', offset=' + offset + ')');
    let params = new HttpParams();
    if (limit != null) {
      params = params.set('limit', String(limit));
    }
    if (offset != null) {
      params = params.set('offset', String(offset));
    }
    return this.httpClient.get<News[]>(this.newsBaseUri, {params});
  }

}
