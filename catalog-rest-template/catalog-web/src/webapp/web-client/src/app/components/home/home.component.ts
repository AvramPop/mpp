import {Component, OnInit} from '@angular/core';
import {Router} from "@angular/router";


@Component({
  selector: 'app-home',
  templateUrl: './home.component.html',
  styleUrls: ['./home.component.css']
})
export class HomeComponent implements OnInit {

  constructor(private router: Router) {}

  ngOnInit() {
  }

  redirectToStudents() {
    this.router.navigateByUrl("/students");
  }

  redirectToLabProblems() {
    this.router.navigateByUrl("/labs");
  }

  redirectToAssignments() {
    this.router.navigateByUrl("/assignments");
  }

  redirectToStats() {
    this.router.navigateByUrl("/stats");
  }
}

