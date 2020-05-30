import {NgModule} from '@angular/core';
import {RouterModule, Routes} from '@angular/router';
import {HomeComponent} from "./components/home/home.component";
import {StudentComponent} from "./components/student/student.component";
import {LabProblemComponent} from "./components/labproblem/lab-problem.component";
import {AssignmentComponent} from "./components/assignment/assignment.component";
import {StatisticsComponent} from "./components/statistics/statistics.component";
import {SortComponent} from "./components/sort/sort.component";
import {LoginComponent} from './components/login/login.component';
import {TeacherAuthGuard} from './teacher-auth';
import {AuthGuard} from './basic-auth';

const routes: Routes = [
  {path: 'home', component: HomeComponent, canActivate: [AuthGuard]},
  {path: 'login', component: LoginComponent},
  {path: 'students', component: StudentComponent, canActivate: [AuthGuard]},
  {path: 'labs', component: LabProblemComponent, canActivate: [AuthGuard]},
  {path: 'assignments', component: AssignmentComponent, canActivate: [TeacherAuthGuard]},
  {path: 'stats', component: StatisticsComponent, canActivate: [AuthGuard]},
  {path: 'sort', component: SortComponent, canActivate: [AuthGuard]},
  // otherwise redirect to home
  {path: '**', redirectTo: 'login'}
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule {
}
