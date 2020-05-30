export class LabProblem {
  id: number;
  description: string;
  problemNumber: number;

  constructor(id: number, description: string, problemNumber: number) {
    this.id = id;
    this.description = description;
    this.problemNumber = problemNumber;
  }
}
