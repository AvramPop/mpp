import { Injectable } from '@angular/core';
import {HttpClient, HttpHeaders, HttpResponse} from "@angular/common/http";
import {Observable, of} from "rxjs";
import {Assignment} from "../../model/assignment";
import {catchError, map} from "rxjs/operators";
import {Sort} from "../../model/dto";

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
    return this.http.get<Assignment[]>(this.url, this.httpOptions)
      .pipe(
        map(result => result['assignments']),
        catchError(this.handleError<Assignment[]>('getAssignments', []))
      );
  }

  getAssignmentById(id: number): Observable<Assignment> {
    return this.http.get<Assignment>(this.url + "/" + id, this.httpOptions)
      .pipe(
        catchError(this.handleError<Assignment>('getAssignment'))
      );
  }

  saveAssignment(assignment: Assignment): Observable<HttpResponse<any>> {
    return this.http.post<HttpResponse<any>>(this.url, assignment, this.httpOptions)
      .pipe(
        catchError(this.handleError<HttpResponse<any>>('saveAssignment'))
      );
  }

  updateAssignment(assignment: Assignment): Observable<HttpResponse<any>> {
    return this.http.put<HttpResponse<any>>(this.url + "/" + assignment.id, assignment, this.httpOptions)
      .pipe(
        catchError(this.handleError<HttpResponse<any>>('updateAssignment'))
      );
  }

  deleteAssignment(id: number): Observable<HttpResponse<any>> {
    return this.http.delete<HttpResponse<any>>(this.url + "/" + id, this.httpOptions)
      .pipe(
        catchError(this.handleError<HttpResponse<any>>('updateAssignment'))
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
