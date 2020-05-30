export class Double {
  value: number;

  constructor(value: number) {
    this.value = value;
  }
}

export class Pair {
  key: any;
  value: any;

  constructor(key: any, value: any) {
    this.key = key;
    this.value = value;
  }
}

export class Sort {
  className: string;
  keys: string[];
  values: string[];

  constructor(className: string, keys: string[], values: string[]) {
    this.className = className;
    this.keys = keys;
    this.values = values;
  }
}

export class Response {
  statusCode: number;

  constructor(statusCode: number){
    this.statusCode = statusCode;
  }
}
