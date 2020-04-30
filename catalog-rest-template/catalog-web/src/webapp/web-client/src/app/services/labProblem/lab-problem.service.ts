import { Injectable } from '@angular/core';
import {HttpClient, HttpHeaders, HttpResponse} from "@angular/common/http";
import {Observable, of} from "rxjs";
import {LabProblem} from "../../model/labProblem";
import {catchError, map} from "rxjs/operators";
import {Sort} from "../../model/dto";
import {Student} from "../../model/student";

@Injectable({
  providedIn: 'root'
})
export class LabProblemService {
  httpOptions = {
    headers: new HttpHeaders({'Content-Type': 'application/json'})
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

  getLabProblemById(id: number): Observable<LabProblem> {
    return this.http.get<LabProblem>(this.url + "/" + id, this.httpOptions)
      .pipe(
        catchError(this.handleError<LabProblem>('getLabProblem'))
      );
  }

  saveLabProblem(labProblem: LabProblem): Observable<HttpResponse<any>> {
    return this.http.post<HttpResponse<any>>(this.url, labProblem, this.httpOptions)
      .pipe(
        catchError(this.handleError<HttpResponse<any>>('saveLabProblem'))
      );
  }

  updateLabProblem(labProblem: LabProblem): Observable<HttpResponse<any>> {
    return this.http.put<HttpResponse<any>>(this.url + "/" + labProblem.id, labProblem, this.httpOptions)
      .pipe(
        catchError(this.handleError<HttpResponse<any>>('updateLabProblem'))
      );
  }

  deleteLabProblem(id: number): Observable<HttpResponse<any>> {
    return this.http.delete<HttpResponse<any>>(this.url + "/" + id, this.httpOptions)
      .pipe(
        catchError(this.handleError<HttpResponse<any>>('deleteLabProblem'))
      );
  }

  getLabProblemsSorted(sort: Sort): Observable<LabProblem[]> {
    return this.http.post<LabProblem[]>(this.url + "/sorted", sort, this.httpOptions)
      .pipe(
        map(result => result['LabProblems']),
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
