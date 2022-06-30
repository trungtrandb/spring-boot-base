import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { environment } from '../../../../environments/environment';
import { User } from '../../../@core/model/user.model';

@Injectable({
  providedIn: 'root',
})
export class UserService {
  public baseUrl = environment.baseUrl + '/users';

  constructor(private httpClient: HttpClient) { }

  public getAll(): Observable<any> {
    return this.httpClient.get<any>(this.baseUrl);
  }

  create(body: any): Observable<any> {
    return this.httpClient.post(this.baseUrl, body);
  }

  getById(id: Number): Observable<User> {
    return this.httpClient.get<User>(this.baseUrl + `/${id}`);
  }

  delete(id: Number): Observable<any> {
    return this.httpClient.delete(this.baseUrl + `/${id}`);
  }
}
