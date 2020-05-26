import { Injectable } from '@angular/core';
import {HttpClient, HttpHeaders, HttpResponse} from "@angular/common/http";
import {Observable, of} from "rxjs";
import {Assignment} from "../../model/assignment";
import {catchError, map, tap} from "rxjs/operators";
import {Response, Sort} from "../../model/dto";

@Injectable({
  providedIn: 'root'
})
export class AssignmentService {
  httpOptions = {
    headers: new HttpHeaders({'Content-Type': 'application/json'})
  };
  private url = 'http://localhost:8082/api/assignments';

  constructor(
    private http: HttpClient) {
  }

  getAssignments(): Observable<Assignment[]> {
    console.log("making call");
    return this.http.get<Assignment[]>(this.url, this.httpOptions)
      .pipe(
        map(result => result['assignments']),
        tap(result => console.log(result)),
        catchError(this.handleError<Assignment[]>('getAssignments', []))
      );
  }

  getAssignmentsPaged(pageIndex: number): Observable<any> {
    return this.http.get<any>(this.url + "/page/" + pageIndex + "/3", this.httpOptions)
      .pipe(
        tap(result => console.log(result)),
        catchError(this.handleError<any>('getAssignmentsPaged', []))
      );
  }

  getAssignmentById(id: number): Observable<Assignment> {
    return this.http.get<Assignment>(this.url + "/" + id, this.httpOptions)
      .pipe(
        catchError(this.handleError<Assignment>('getAssignment'))
      );
  }

  saveAssignment(assignment: Assignment): Observable<Response> {
    return this.http.post<Response>(this.url, assignment, this.httpOptions)
      .pipe(
        catchError(this.handleError<Response>('saveAssignment'))
      );
  }

  updateAssignment(assignment: Assignment): Observable<Response> {
    return this.http.put<Response>(this.url + "/" + assignment.id, assignment, this.httpOptions)
      .pipe(
        catchError(this.handleError<Response>('updateAssignment'))
      );
  }

  deleteAssignment(id: number): Observable<Response> {
    return this.http.delete<Response>(this.url + "/" + id, this.httpOptions)
      .pipe(
        catchError(this.handleError<Response>('updateAssignment'))
      );
  }

  getAssignmentsSorted(sort: Sort): Observable<Assignment[]> {
    return this.http.post<Assignment[]>(this.url + "/sorted", sort, this.httpOptions)
      .pipe(
        map(result => result['assignments']),
        catchError(this.handleError<Assignment[]>('getAssignmentsSorted', []))
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
