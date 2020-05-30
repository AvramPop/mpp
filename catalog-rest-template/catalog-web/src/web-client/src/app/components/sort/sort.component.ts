import {Component, OnInit} from '@angular/core';
import {FormArray, FormBuilder, FormControl, FormGroup} from "@angular/forms";
import {StudentService} from "../../services/student/student.service";
import {AssignmentService} from "../../services/assignment/assignment.service";
import {LabProblemService} from "../../services/labProblem/lab-problem.service";
import {Student} from "../../model/student";
import {LabProblem} from "../../model/labProblem";
import {Assignment} from "../../model/assignment";
import {Subscription} from "rxjs";
import {Sort} from "../../model/dto";

enum SortOrder {DESC, ASC}

@Component({
  selector: 'app-sort',
  templateUrl: './sort.component.html',
  styleUrls: ['./sort.component.css']
})
export class SortComponent implements OnInit {
  classes: string[] = ['LabProblem', 'Student', 'Assignment'];
  studentsSortedReady: boolean;
  studentsSorted: Student[];
  labProblemsSortedReady: boolean;
  labProblemsSorted: LabProblem[];
  studentsDisplayedColumns: string[] = ['id', 'name', 'serialNumber', 'group'];
  assignmentDisplayedColumns: string[] = ['id', 'studentId', 'labProblemId', 'grade'];
  labProblemsDisplayedColumns: string[] = ['id', 'problemNumber', 'description'];
  assignmentsSortedReady: boolean;
  assignmentsSorted: Assignment[];
  sortData: string;
  sortDataParsed: string[];
  selectedClassName = "None";

  constructor(private studentService: StudentService,
                           private assignmentService: AssignmentService, private labProblemService: LabProblemService) {
  }
  ngOnInit() {}

  sort() {
    console.log(this.sortData);
    this.sortDataParsed = this.sortData.split(',');
    console.log(this.sortDataParsed);
    var sort = new Sort(this.selectedClassName, [], []);
    for(let i = 0; i < this.sortDataParsed.length; i += 2) {
      sort.keys.push(this.sortDataParsed[i]);
      sort.values.push(this.sortDataParsed[i + 1]);
    }
    console.log(sort);
    if(this.selectedClassName == "Student"){
      this.studentsSortedReady = true;
      this.assignmentsSortedReady = false;
      this.labProblemsSortedReady = false;
      this.studentService.getStudentsSorted(sort).subscribe(result => {
        this.studentsSorted = result;
        console.log(result);
      });
    } else if(this.selectedClassName == "LabProblem") {
      this.studentsSortedReady = false;
      this.assignmentsSortedReady = false;
      this.labProblemsSortedReady = true;
      this.labProblemService.getLabProblemsSorted(sort).subscribe(result => this.labProblemsSorted = result);
    } else {
      this.studentsSortedReady = false;
      this.assignmentsSortedReady = true;
      this.labProblemsSortedReady = false;
      this.assignmentService.getAssignmentsSorted(sort).subscribe(result => this.assignmentsSorted = result);

    }
  }
}
