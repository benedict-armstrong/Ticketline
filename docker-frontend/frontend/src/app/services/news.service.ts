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
    // console.log('Create news with title ' + news.title);
    return this.httpClient.post<News>(this.newsBaseUri, news);
  }

  /**
   * Returns an array of news
   *
   * @param page the amount of news to be returned
   * @param size the offset parameter for pagination
   */
  getNews(page: number, size: number): Observable<News[]> {
    let params = new HttpParams();
    params = params.set('page', String(page));
    params = params.set('size', String(size));
    return this.httpClient.get<News[]>(this.newsBaseUri, { params });
  }

  /**
   * Loads specific news from the backend
   *
   * @param id of news to load
   */
  getNewsById(id: number): Observable<News> {
    // console.log('Load news details for ' + id);
    return this.httpClient.get<News>(this.newsBaseUri + '/' + id);
  }

}


