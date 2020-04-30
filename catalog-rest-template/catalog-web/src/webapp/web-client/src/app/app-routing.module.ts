import {NgModule} from '@angular/core';
import {RouterModule, Routes} from '@angular/router';
import {HomeComponent} from "./components/home/home.component";
import {StudentComponent} from "./components/student/student.component";
import {LabProblemComponent} from "./components/labproblem/lab-problem.component";
import {AssignmentComponent} from "./components/assignment/assignment.component";
import {StatisticsComponent} from "./components/statistics/statistics.component";
import {SortComponent} from "./components/sort/sort.component";

const routes: Routes = [
  {path: 'home', component: HomeComponent},
  {path: 'students', component: StudentComponent},
  {path: 'labs', component: LabProblemComponent},
  {path: 'assignments', component: AssignmentComponent},
  {path: 'stats', component: StatisticsComponent},
  {path: 'sort', component: SortComponent},
  // otherwise redirect to home
  {path: '**', redirectTo: 'home'}
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule {
}
