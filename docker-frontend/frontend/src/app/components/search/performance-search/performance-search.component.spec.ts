import { ComponentFixture, TestBed } from '@angular/core/testing';

import { PerformanceSearchComponent } from './performance-search.component';

describe('PerformanceSearchComponent', () => {
  let component: PerformanceSearchComponent;
  let fixture: ComponentFixture<PerformanceSearchComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ PerformanceSearchComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(PerformanceSearchComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
