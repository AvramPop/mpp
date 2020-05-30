import { Injectable } from '@angular/core';
import {HttpClient, HttpHeaders, HttpResponse} from "@angular/common/http";
import {Observable, of} from "rxjs";
import {LabProblem} from "../../model/labProblem";
import {catchError, map, tap} from "rxjs/operators";
import {Response, Sort} from "../../model/dto";
import {Student} from "../../model/student";

@Injectable({
  providedIn: 'root'
})
export class LabProblemService {
  httpOptions = {
    headers: new HttpHeaders({'Content-Type': 'application/x-www-form-urlencoded'}),
    // headers: new HttpHeaders({'Content-Type': 'application/json'}),

    withCredentials: true
  };
  private url = 'http://localhost:8082/api/labs';

  constructor(
    private http: HttpClient) {
  }

  getLabProblemsByNumber(problemNumber: number): Observable<LabProblem[]> {
    return this.http.get<LabProblem[]>(this.url + "/bynumber/" + problemNumber, this.httpOptions)
      .pipe(
        map(result => result['labProblems']),
        catchError(this.handleError<LabProblem[]>('getLabProblemsByProblemNumber', []))
      );
  }

  getLabProblems(): Observable<LabProblem[]> {
    return this.http.get<LabProblem[]>(this.url, this.httpOptions)
      .pipe(
        map(result => result['labProblems']),
        catchError(this.handleError<LabProblem[]>('getLabProblems', []))
      );
  }

  getLabProblemsPaged(pageIndex: number): Observable<any> {
    return this.http.get<any>(this.url + "/page/" + pageIndex + "/3", this.httpOptions)
      .pipe(
        tap(result => console.log(result)),
        catchError(this.handleError<any>('getLabProblemsPaged', []))
      );
  }

  getLabProblemById(id: number): Observable<LabProblem> {
    return this.http.get<LabProblem>(this.url + "/" + id, this.httpOptions)
      .pipe(
        catchError(this.handleError<LabProblem>('getLabProblem'))
      );
  }

  saveLabProblem(labProblem: LabProblem): Observable<Response> {
    return this.http.post<Response>(this.url, labProblem, this.httpOptions)
      .pipe(
        catchError(this.handleError<Response>('saveLabProblem'))
      );
  }

  updateLabProblem(labProblem: LabProblem): Observable<Response> {
    return this.http.put<Response>(this.url + "/" + labProblem.id, labProblem, this.httpOptions)
      .pipe(
        catchError(this.handleError<Response>('updateLabProblem'))
      );
  }

  deleteLabProblem(id: number): Observable<Response> {
    return this.http.delete<Response>(this.url + "/" + id, this.httpOptions)
      .pipe(
        catchError(this.handleError<Response>('deleteLabProblem'))
      );
  }

  getLabProblemsSorted(sort: Sort): Observable<LabProblem[]> {
    return this.http.post<LabProblem[]>(this.url + "/sorted", sort, this.httpOptions)
      .pipe(
        map(result => result['labProblems']),
        catchError(this.handleError<LabProblem[]>('getLabProblemsSorted', []))
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
}
