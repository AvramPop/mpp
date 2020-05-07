import { Injectable } from '@angular/core';
import {HttpClient, HttpHeaders, HttpResponse} from "@angular/common/http";
import {Observable, of, pipe} from "rxjs";
import {Student} from "../../model/student";
import {catchError, map, tap} from "rxjs/operators";
import {Response, Sort} from "../../model/dto";
import {Assignment} from "../../model/assignment";

@Injectable({
  providedIn: 'root'
})
export class StudentService {
  httpOptions = {
    headers: new HttpHeaders({'Content-Type': 'application/json'})
  };
  private url = 'http://localhost:8082/api/students';

  constructor(
    private http: HttpClient) {
  }

  getStudentsByGroup(groupNumber: number): Observable<Student[]> {
    return this.http.get<Student[]>(this.url + "/group/" + groupNumber, this.httpOptions)
      .pipe(
        map(result => result['students']),
        catchError(this.handleError<Student[]>('getStudentsByGroupNUmber', []))
      );
  }
  getStudentsSorted(sort: Sort): Observable<Student[]> {
    return this.http.post<Student[]>(this.url + "/sorted", sort, this.httpOptions)
      .pipe(
        map(result => result['students']),
        catchError(this.handleError<Student[]>('getStudentsSorted', []))
      );
  }

  getStudents(): Observable<Student[]> {
    return this.http.get<Student[]>(this.url, this.httpOptions)
      .pipe(
        tap(result => console.log(result)),
        map(result => result['students']),
        catchError(this.handleError<Student[]>('getStudents', []))
      );
  }
  getStudentsPaged(pageIndex: number): Observable<any> {
    return this.http.get<any>(this.url + "/page/" + pageIndex + "/3", this.httpOptions)
      .pipe(
        tap(result => console.log(result)),
        catchError(this.handleError<any>('getStudentsPaged', []))
      );
  }

  saveStudent(student: Student): Observable<Response> {
    return this.http.post<Response>(this.url, student, this.httpOptions)
      .pipe(
        catchError(this.handleError<Response>('saveStudent'))
      );
  }

  getStudentById(id: number): Observable<Student> {
    return this.http.get<Student>(this.url + "/" + id, this.httpOptions)
      .pipe(
        catchError(this.handleError<Student>('getStudent'))
      );
  }

  updateStudent(student: Student): Observable<Response> {
    return this.http.put<Response>(this.url + "/" + student.id, student, this.httpOptions)
      .pipe(
        catchError(this.handleError<Response>('updateStudent'))
      );
  }

  deleteStudent(id: number): Observable<Response> {
    return this.http.delete<Response>(this.url + "/" + id, this.httpOptions)
      .pipe(
        catchError(this.handleError<Response>('deleteStudent'))
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
