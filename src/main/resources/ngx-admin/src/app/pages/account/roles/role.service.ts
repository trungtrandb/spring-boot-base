import { HttpClient, HttpParams } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { environment } from '../../../../environments/environment';

@Injectable({
  providedIn: 'root',
})
export class RoleService {
  public baseUrl = environment.baseUrl + '/auth/roles';

  constructor(private httpClient: HttpClient) { }

  public getAll(): Observable<any> {
    return this.httpClient.get<any>(
      this.baseUrl,
      {
        params: new HttpParams().set('_limit', 1000),
      },
    );
  }

  create(body: any): Observable<any> {
    return this.httpClient.post(this.baseUrl, body);
  }

  delete(id: Number): Observable<any> {
    return this.httpClient.delete(this.baseUrl + `/${id}`);
  }

  getAllPrivilege(): Observable<any> {
    return this.httpClient.get(environment.baseUrl + '/auth/privileges');
  }
}