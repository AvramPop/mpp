import { Component, OnInit } from '@angular/core';
import {Student} from "../../model/student";
import {Observable} from "rxjs";
import {Double, Pair} from "../../model/dto";
import {catchError} from "rxjs/operators";
import {StatisticsService} from "../../services/statistics/statistics.service";
import {StudentService} from "../../services/student/student.service";

@Component({
  selector: 'app-statistics',
  templateUrl: './statistics.component.html',
  styleUrls: ['./statistics.component.css']
})
export class StatisticsComponent implements OnInit {
  averageMean: number;
  greatestMean: number;
  studentWithGreatestMean: Student;
  idOfLabProblemMostAssigned: number;
  timesMostProblemAssigned: number;

  constructor(private statisticsService: StatisticsService, private studentService: StudentService) { }

  ngOnInit(): void {
    this.getStats();
  }

  private getStats() {
    this.getAverageMean();
    this.getGreatestMean();
    this.getLabProblemMostAssigned();
  }

  private getAverageMean() {
    this.statisticsService.getAverageGrade().subscribe(result => this.averageMean = result.value);
  }

  private getGreatestMean() {
    this.statisticsService.getGreatestMean().subscribe(result => {
      this.studentService.getStudentById(result.key).subscribe(studentWithGreatestResult => {
        this.studentWithGreatestMean = studentWithGreatestResult;
      })
      this.greatestMean = result.value;
    }
  )
  }

  private getLabProblemMostAssigned() {
    this.statisticsService.getIdOfLabProblemMostAssigned().subscribe(result => {
      this.idOfLabProblemMostAssigned = result.key;
      this.timesMostProblemAssigned = result.value;
    })
  }
}
