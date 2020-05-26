import {Component, OnInit, ViewChild} from '@angular/core';
import {Assignment} from "../../model/assignment";
import {AssignmentService} from "../../services/assignment/assignment.service";
import {MatSort} from "@angular/material/sort";
import {MatTableDataSource} from "@angular/material/table";
import {Student} from "../../model/student";

@Component({
  selector: 'app-assignment',
  templateUrl: './assignment.component.html',
  styleUrls: ['./assignment.component.css']
})
export class AssignmentComponent implements OnInit {
  assignments: Assignment[];
  displayedColumns: string[] = ['id', 'studentId', 'labProblemId', 'grade'];
  idToAdd: number;
  studentIdToAdd: number;
  labProblemIdToAdd: number;
  gradeToAdd: number;
  idToDelete: number;
  idToUpdate: number;
  studentIdToUpdate: number;
  labProblemIdToUpdate: number;
  gradeToUpdate: number;
  badAdd: boolean = false;
  badDelete: boolean = false;
  badUpdate: boolean = false;
  dataSource: any;
  currentPage = 0;
  @ViewChild(MatSort, {static: true}) sort: MatSort;
  totalNumberOfAssignments: number;
  constructor(private assignmentService: AssignmentService) { }

  ngOnInit() {
    this.setTotalAssignments();
    this.getAssignmentsLoad();
  }

  getAssignmentsLoad(): void {
    //this.assignmentService.getAssignmentsPaged(0)
    this.assignmentService.getAssignments()
      .subscribe(assignments => {
        console.log(assignments);
        this.dataSource = new MatTableDataSource<Assignment>(assignments);
        this.dataSource.sort = this.sort;
        // this.assignments = assignments["assignments"]
      });
  }

  getAssignments(event?): void {
   // this.assignmentService.getAssignmentsPaged(event.pageIndex)
    this.assignmentService.getAssignments()
      .subscribe(assignments => {
        this.dataSource = new MatTableDataSource<Assignment>(assignments);
        this.dataSource.sort = this.sort;
        // this.assignments = assignments["assignments"];
        this.currentPage = assignments["pageNumber"];
      });
  }

  applyFilter(event: Event) {
    const filterValue = (event.target as HTMLInputElement).value;
    this.dataSource.filter = filterValue.trim().toLowerCase();
  }

  addAssignment() {
    if(this.validAddData()) {
      this.assignmentService.saveAssignment(new Assignment(this.idToAdd, this.studentIdToAdd, this.labProblemIdToAdd, this.gradeToAdd))
        .subscribe(response => {
          if (response.statusCode == 404) {
            this.badAdd = true;
          } else {
            this.currentPage = 0;

            this.getAssignmentsLoad();
            this.setTotalAssignments();

            this.badAdd = false;
          }
        })
    } else {
      this.badAdd = true;
    }
  }

  deleteAssignment() {
    if(this.validDeleteData()) {
      this.assignmentService.deleteAssignment(this.idToDelete)
        .subscribe(response => {
          if (response.statusCode == 404) {
            this.badDelete = true;
          } else {
            this.currentPage = 0;

            this.getAssignmentsLoad();
            this.setTotalAssignments();

            this.badDelete = false;
          }
        })
    } else {
      this.badDelete = true;
    }
  }

  updateAssignment() {
    if(this.validUpdateData()) {
      this.assignmentService.updateAssignment(new Assignment(this.idToUpdate, this.studentIdToUpdate, this.labProblemIdToUpdate, this.gradeToUpdate))
        .subscribe(response => {
          if (response.statusCode == 404) {
            this.badUpdate = true;
          } else {
            this.currentPage = 0;

            this.getAssignmentsLoad();
            this.setTotalAssignments();

            this.badUpdate = false;
          }
        })
    } else {
      this.badUpdate = true;
    }
  }

  private validAddData():boolean {
    return !isNaN(this.idToAdd) && !isNaN(this.studentIdToAdd) && !isNaN(this.labProblemIdToAdd) && !isNaN(this.gradeToAdd);
  }

  private validUpdateData():boolean {
    return !isNaN(this.idToUpdate) && !isNaN(this.studentIdToUpdate) && !isNaN(this.labProblemIdToUpdate) && !isNaN(this.gradeToUpdate);
  }

  private validDeleteData():boolean {
    return !isNaN(this.idToDelete);
  }

  private setTotalAssignments() {

    this.assignmentService.getAssignments().subscribe(response => this.totalNumberOfAssignments = response.length);
  }

}
