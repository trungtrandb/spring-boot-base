import { HttpClient, HttpParams } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { environment } from '../../../environments/environment';
import { Post } from '../../@core/model/post.model';

@Injectable({
  providedIn: 'root',
})
export class PostService {
  public baseUrl = environment.baseUrl + '/posts';

  constructor(private httpClient: HttpClient) { }

  create(body: any): Observable<Post> {
    return this.httpClient.post<Post>(this.baseUrl, body);
  }

  delete(id: Number): Observable<any> {
    return this.httpClient.delete(this.baseUrl + `/${id}`, {observe : 'response'});
  }

  getById(id: Number): Observable<Post> {
    return this.httpClient.get<Post>(this.baseUrl + `/${id}`);
  }

  getAllCategory(): Observable<any> {
    return this.httpClient.get(environment.baseUrl + '/categories');
  }
}
