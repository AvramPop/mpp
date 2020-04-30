import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { LabProblemComponent } from './lab-problem.component';

describe('LabproblemComponent', () => {
  let component: LabProblemComponent;
  let fixture: ComponentFixture<LabProblemComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ LabProblemComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(LabProblemComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
