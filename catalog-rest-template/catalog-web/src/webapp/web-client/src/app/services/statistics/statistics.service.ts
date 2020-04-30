import { Injectable } from '@angular/core';
import {HttpClient, HttpHeaders, HttpResponse} from "@angular/common/http";
import {Observable, of} from "rxjs";
import {LabProblem} from "../../model/labProblem";
import {catchError, map} from "rxjs/operators";
import {Double, Pair, Sort} from "../../model/dto";

@Injectable({
  providedIn: 'root'
})
export class StatisticsService {
  httpOptions = {
    headers: new HttpHeaders({'Content-Type': 'application/json'})
  };
  private url = 'http://localhost:8082/api/logic';

  constructor(
    private http: HttpClient) {
  }

  getIdOfLabProblemMostAssigned(): Observable<Pair> {
    return this.http.get<Pair>(this.url + "/assigned", this.httpOptions)
      .pipe(
        catchError(this.handleError<Pair>('getIdOfLabProblemMostAssigned'))
      );
  }

  getGreatestMean(): Observable<Pair> {
    return this.http.get<Pair>(this.url + "/mean", this.httpOptions)
      .pipe(
        catchError(this.handleError<Pair>('getGreatestMean'))
      );
  }

  getAverageGrade(): Observable<Double> {
    return this.http.get<Double>(this.url + "/avg", this.httpOptions)
      .pipe(
        catchError(this.handleError<Double>('getAverageGrade'))
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
