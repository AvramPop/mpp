import {NgModule} from '@angular/core';
import {BrowserModule} from '@angular/platform-browser';
import {FormsModule, ReactiveFormsModule} from '@angular/forms';
import {BrowserAnimationsModule} from '@angular/platform-browser/animations';
import {MatInputModule} from '@angular/material/input';
import {MAT_DATE_LOCALE, MatNativeDateModule} from '@angular/material/core';
import {MatDatepickerModule} from '@angular/material/datepicker';
import {MatCardModule} from '@angular/material/card';
import {NgxMaterialTimepickerModule} from 'ngx-material-timepicker';
import {MatButtonModule} from '@angular/material/button';
import {AppComponent} from './components/app/app.component';
import {HttpClientModule} from '@angular/common/http';
import {MatSnackBarModule} from '@angular/material/snack-bar';
import {AppRoutingModule} from './app-routing.module';
import {AmazingTimePickerModule} from 'amazing-time-picker'; // this line you need
import {MatListModule} from '@angular/material/list';
import {MAT_FORM_FIELD_DEFAULT_OPTIONS, MatFormFieldModule} from '@angular/material/form-field';
import {MatIconModule} from '@angular/material/icon';
import {MatSliderModule} from '@angular/material/slider';
import {MatSlideToggleModule} from '@angular/material/slide-toggle';
import {MatCheckboxModule} from '@angular/material/checkbox';
import {HomeComponent} from "./components/home/home.component";
import {MatSelectModule} from '@angular/material/select';
import { StudentComponent } from './components/student/student.component';
import { AssignmentComponent } from './components/assignment/assignment.component';
import { LabProblemComponent } from './components/labproblem/lab-problem.component';
import { StatisticsComponent } from './components/statistics/statistics.component';
import {MatTableModule} from "@angular/material/table";
import { SortComponent } from './components/sort/sort.component';


@NgModule({
  imports: [
    BrowserModule,
    FormsModule,
    HttpClientModule,
    AppRoutingModule,
    MatButtonModule,
    BrowserAnimationsModule,
    MatInputModule,
    MatDatepickerModule,
    MatIconModule,
    MatListModule,
    MatSnackBarModule,
    AmazingTimePickerModule,
    ReactiveFormsModule,
    MatCardModule,
    MatNativeDateModule,
    NgxMaterialTimepickerModule,
    MatSliderModule,
    MatSlideToggleModule,
    MatCheckboxModule,

    MatFormFieldModule,
    MatSelectModule,
    MatTableModule
  ],
  providers: [
    {provide: MAT_FORM_FIELD_DEFAULT_OPTIONS, useValue: {appearance: 'fill'}},
    {provide: MAT_DATE_LOCALE, useValue: 'ro-RO'}
  ],
  declarations: [
    AppComponent,
    AppComponent,
    HomeComponent,
    StudentComponent,
    AssignmentComponent,
    LabProblemComponent,
    StatisticsComponent,
    SortComponent,
  ],
  bootstrap: [AppComponent]
})
export class AppModule {
}
