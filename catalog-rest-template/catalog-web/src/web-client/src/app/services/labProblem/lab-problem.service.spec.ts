import { TestBed } from '@angular/core/testing';

import { LabProblemService } from './lab-problem.service';

describe('LabProblemService', () => {
  let service: LabProblemService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(LabProblemService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
