import { Component, OnInit } from '@angular/core';
import {Student} from "../../model/student";
import {StudentService} from "../../services/student/student.service";
import {Router} from "@angular/router";

@Component({
  selector: 'app-student',
  templateUrl: './student.component.html',
  styleUrls: ['./student.component.css']
})
export class StudentComponent implements OnInit {
  students: Student[];
  displayedColumns: string[] = ['id', 'name', 'serialNumber', 'group'];
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
  constructor(private studentService: StudentService) { }

  ngOnInit() {
    this.getStudents();
  }

  getStudents(): void {
    this.studentService.getStudents()
      .subscribe(students => this.students = students);
  }

  addStudent() {
    if(this.validAddData()) {
      this.studentService.saveStudent(new Student(this.idToAdd, this.serialNumberToAdd, this.nameToAdd, this.groupToAdd))
        .subscribe(response => {
          if (response.statusCode == 404) {
            this.badAdd = true;
          } else {
            this.getStudents();
            this.badAdd = false;
          }
        })
    } else {
      this.badAdd = true;
    }

  }

  deleteStudent() {
    window.location.reload();
  }

  updateStudent() {
    window.location.reload();
  }

  private validAddData():boolean {
    return !isNaN(this.idToAdd) && !isNaN(this.groupToAdd);

  }
}
