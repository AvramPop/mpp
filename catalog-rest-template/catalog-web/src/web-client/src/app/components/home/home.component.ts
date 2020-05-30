import {Component, OnInit} from '@angular/core';
import {Router} from "@angular/router";
import {LoginService} from '../../services/login/login.service';


@Component({
  selector: 'app-home',
  templateUrl: './home.component.html',
  styleUrls: ['./home.component.css']
})
export class HomeComponent implements OnInit {
  isTeacher = false;

  constructor(private router: Router, private loginService: LoginService) {
    this.loginService.getCurrentUser().subscribe(result => {
      console.log(result);
      this.isTeacher = result === 'ROLE_TEACHER';
    });
  }

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

  redirectToSort() {
    this.router.navigateByUrl("/sort");
  }

  logout() {
    this.loginService.logout().subscribe(response => this.router.navigateByUrl("/login"));
  }
}

