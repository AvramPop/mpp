import { Component, OnInit } from '@angular/core';
import {LabProblem} from "../../model/labProblem";
import {LabProblemService} from "../../services/labProblem/lab-problem.service";

@Component({
  selector: 'app-labproblem',
  templateUrl: './lab-problem.component.html',
  styleUrls: ['./lab-problem.component.css']
})
export class LabProblemComponent implements OnInit {

  labProblems: LabProblem[];
  displayedColumns: string[] = ['id', 'problemNumber', 'description'];
  idToAdd: number;
  descriptionToAdd: string;
  problemNumberToAdd: number;
  idToDelete: number;
  idToUpdate: number;
  descriptionToUpdate: string;
  problemNumberToUpdate: number;
  badAdd: boolean = false;
  badDelete: boolean = false;
  badUpdate: boolean = false;
  problemNumberToFilterBy: number;
  badFilterInput: boolean = false;
  filtered: boolean = false;
  labProblemsFiltered: LabProblem[];
  constructor(private labProblemService: LabProblemService) { }

  ngOnInit() {
    this.getLabProblems();
  }

  getLabProblems(): void {
    this.labProblemService.getLabProblems()
      .subscribe(labProblems => this.labProblems = labProblems);
  }

  addLabProblem() {
    if(this.validAddData()) {
      this.labProblemService.saveLabProblem(new LabProblem(this.idToAdd, this.descriptionToAdd, this.problemNumberToAdd))
        .subscribe(response => {
          if (response.statusCode == 404) {
            this.badAdd = true;
          } else {
            this.getLabProblems();
            this.badAdd = false;
          }
        })
    } else {
      this.badAdd = true;
    }
  }

  deleteLabProblem() {
    if(this.validDeleteData()) {
      this.labProblemService.deleteLabProblem(this.idToDelete)
        .subscribe(response => {
          if (response.statusCode == 404) {
            this.badDelete = true;
          } else {
            this.getLabProblems();
            this.badDelete = false;
          }
        })
    } else {
      this.badDelete = true;
    }
  }

  updateLabProblem() {
    if(this.validUpdateData()) {
      this.labProblemService.updateLabProblem(new LabProblem(this.idToUpdate, this.descriptionToUpdate, this.problemNumberToUpdate))
        .subscribe(response => {
          if (response.statusCode == 404) {
            this.badUpdate = true;
          } else {
            this.getLabProblems();
            this.badUpdate = false;
          }
        })
    } else {
      this.badUpdate = true;
    }
  }

  private validAddData():boolean {
    return !isNaN(this.idToAdd) && !isNaN(this.problemNumberToAdd);
  }

  private validUpdateData():boolean {
    return !isNaN(this.idToUpdate) && !isNaN(this.problemNumberToUpdate);
  }

  private validDeleteData():boolean {
    return !isNaN(this.idToDelete);
  }

  private validFilterData():boolean {
    return !isNaN(this.problemNumberToFilterBy);
  }

  filterLabProblems() {
    if(this.validFilterData()) {
      this.labProblemService.getLabProblemsByNumber(this.problemNumberToFilterBy).subscribe(
        result => {
          this.badFilterInput = false;
          this.filtered = true;
          this.labProblemsFiltered = result;
        }
      )
    } else {
      this.badFilterInput = true;
      this.filtered = false;
    }
  }
}
