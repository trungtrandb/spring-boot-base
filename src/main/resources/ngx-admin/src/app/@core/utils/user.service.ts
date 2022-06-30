import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { environment } from '../../../environments/environment';
import { User } from '../model/user.model';

@Injectable({
  providedIn: 'root',
})
export class UserService {
  public baseUrl = environment.baseUrl + '/users';

  constructor(private httpClient: HttpClient) { }

  public getProfile(): Observable<User> {
    return this.httpClient.get<User>(this.baseUrl + '/profile');
  }
}
