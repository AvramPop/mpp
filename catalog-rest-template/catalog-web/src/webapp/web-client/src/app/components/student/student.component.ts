import {Component, OnInit, ViewChild} from '@angular/core';
import {Student} from "../../model/student";
import {StudentService} from "../../services/student/student.service";
import {Router} from "@angular/router";
import {PageEvent} from "@angular/material/paginator";
import {MatSort} from "@angular/material/sort";
import {MatTableDataSource} from "@angular/material/table";

@Component({
  selector: 'app-student',
  templateUrl: './student.component.html',
  styleUrls: ['./student.component.css']
})
export class StudentComponent implements OnInit {
  students: Student[];
  displayedColumns: string[] = ['id', 'name', 'serialNumber', 'studentGroup'];
  idToAdd: number;
  nameToAdd: string;
  serialNumberToAdd: string;
  groupToAdd: number;
  idToDelete: number;
  idToUpdate: number;
  nameToUpdate: string;
  serialNumberToUpdate: string;
  groupToUpdate: number;
  badAdd: boolean = false;
  badDelete: boolean = false;
  badUpdate: boolean = false;
  groupNumberToFilterBy: number;
  badFilterInput: boolean = false;
  filtered: boolean = false;
  studentsFiltered: Student[];
  dataSource: any;
  currentPage = 0;
  @ViewChild(MatSort, {static: true}) sort: MatSort;
  totalNumberOfStudents: number;
  constructor(private studentService: StudentService) { }

  ngOnInit() {
    this.setTotalStudents();
    this.getStudentsLoad();
  }

  getStudentsLoad(): void {
    this.studentService.getStudentsPaged(0)
      .subscribe(students => {
        this.dataSource = new MatTableDataSource<Student>(students["students"]);
        this.dataSource.sort = this.sort;
     //   this.students = students["students"]
      });
  }

  getStudents(event?): void {
    this.studentService.getStudentsPaged(event.pageIndex)
      .subscribe(students => {
        // this.students = students["students"];
        this.dataSource = new MatTableDataSource<Student>(students["students"]);
        this.dataSource.sort = this.sort;
        this.currentPage = students["pageNumber"];
      });
  }

  applyFilter(event: Event) {
    const filterValue = (event.target as HTMLInputElement).value;
    this.dataSource.filter = filterValue.trim().toLowerCase();
  }

  addStudent() {
    if(this.validAddData()) {
      this.studentService.saveStudent(new Student(this.idToAdd, this.serialNumberToAdd, this.nameToAdd, this.groupToAdd))
        .subscribe(response => {
          if (response.statusCode == 404) {
            this.badAdd = true;
          } else {
            this.currentPage = 0;
            this.getStudentsLoad();
            this.setTotalStudents();

            this.badAdd = false;
          }
        })
    } else {
      this.badAdd = true;
    }
  }

  deleteStudent() {
    if(this.validDeleteData()) {
      this.studentService.deleteStudent(this.idToDelete)
        .subscribe(response => {
          if (response.statusCode == 404) {
            this.badDelete = true;
          } else {
            this.currentPage = 0;
            this.getStudentsLoad();
            this.setTotalStudents();

            this.badDelete = false;
          }
        })
    } else {
      this.badDelete = true;
    }
  }

  updateStudent() {
    if(this.validUpdateData()) {
      this.studentService.updateStudent(new Student(this.idToUpdate, this.serialNumberToUpdate, this.nameToUpdate, this.groupToUpdate))
        .subscribe(response => {
          if (response.statusCode == 404) {
            this.badUpdate = true;
          } else {
            this.currentPage = 0;
            this.getStudentsLoad();
            this.setTotalStudents();

            this.badUpdate = false;
          }
        })
    } else {
      this.badUpdate = true;
    }
  }

  private validAddData():boolean {
    return !isNaN(this.idToAdd) && !isNaN(this.groupToAdd);
  }

  private validUpdateData():boolean {
    return !isNaN(this.idToUpdate) && !isNaN(this.groupToUpdate);
  }

  private validDeleteData():boolean {
    return !isNaN(this.idToDelete);
  }

  private validFilterData():boolean {
    return !isNaN(this.groupNumberToFilterBy);
  }

  filterStudents() {
    if(this.validFilterData()) {
      this.studentService.getStudentsByGroup(this.groupNumberToFilterBy).subscribe(
        result => {
          this.badFilterInput = false;
          this.filtered = true;
          this.studentsFiltered = result;
        }
      )
    } else {
      this.badFilterInput = true;
      this.filtered = false;
    }
  }

  private setTotalStudents() {

    this.studentService.getStudents().subscribe(response => this.totalNumberOfStudents = response.length);
  }

  sortClient() {
    // //this.students = [new Student(1, 'a', 'a', 1), new Student(2, 'a', 'a', 1)];
    // console.log(this.students);
    // // console.log(this.students);
    // const temp = this.students;
    // temp.sort((a, b) => (a.name > b.name ? -1 : 1));
    // this.students = temp;
    // console.log(this.students);
    this.studentService.getStudentsPaged(this.currentPage).subscribe(
      students => {
      const temp = students["students"];
      temp.sort((a, b) => a.name > b.name ? -1 : 1);
      this.students = temp;
      this.currentPage = students["pageNumber"];
    })
  }
}
