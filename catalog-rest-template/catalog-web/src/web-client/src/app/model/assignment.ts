export class Assignment {
  id: number;
  studentId: number;
  labProblemId: number;
  grade: number;

  constructor(id: number, studentId: number, labProblemId: number, grade: number) {
    this.id = id;
    this.studentId = studentId;
    this.labProblemId = labProblemId;
    this.grade = grade;
  }
}
