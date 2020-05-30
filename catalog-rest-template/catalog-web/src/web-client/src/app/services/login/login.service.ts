import { Injectable } from '@angular/core';
import {HttpClient, HttpHeaders} from '@angular/common/http';
import {Observable, of} from 'rxjs';
import {Student} from '../../model/student';
import {catchError, map, tap} from 'rxjs/operators';
import {Response, Sort} from '../../model/dto';
import {browser} from 'protractor';

@Injectable({
  providedIn: 'root'
})
export class LoginService {
  httpOptions = {
    headers: new HttpHeaders({'Content-Type': 'application/x-www-form-urlencoded'}),
    withCredentials: true, observe: 'response'
  };
  private url = 'http://localhost:8082/login';

  currentUser: string = "NONE";

  constructor(
    private http: HttpClient) {
  }

  public login(username: string, password: string): Observable<boolean> {


      return this.http.post(this.url + '?username=' + username + '&password=' + password, {}, this.httpOptions)
        .pipe(
          map(result => {
            this.getCurrentUser().subscribe(credentialsLogin => this.currentUser = credentialsLogin);
            return result.status === 200;
          })
        );
  }

  private handleError<T>(operation = 'operation', result?: T) {
    return (error: any): Observable<T> => {

      // TODO: send the error to remote logging infrastructure
      console.error(error); // log to console instead

      // Let the app keep running by returning an empty result.
      return of(result as T);
    };
  }

  getCurrentUser(): Observable<string> {
    return this.http.get("http://localhost:8082/api/user", {withCredentials: true})
      .pipe(
        map(response => {
          return response["type"];
        })
      );
  }

  logout(): Observable<any> {
     return this.http.post("http://localhost:8082/logout", {}, this.httpOptions);
  }

}
