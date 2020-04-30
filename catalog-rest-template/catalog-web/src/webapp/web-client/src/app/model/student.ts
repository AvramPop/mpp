export class Student {
  id: number;
  serialNumber: string;
  name: string;
  studentGroup: number;

  constructor(id: number, serialNumber: string, name: string, studentGroup: number) {
    this.id = id;
    this.serialNumber = serialNumber;
    this.name = name;
    this.studentGroup = studentGroup;
  }
}
