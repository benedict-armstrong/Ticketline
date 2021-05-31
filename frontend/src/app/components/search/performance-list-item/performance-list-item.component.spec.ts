import { ComponentFixture, TestBed } from '@angular/core/testing';

import { PerformanceListItemComponent } from './performance-list-item.component';

describe('PerformanceListItemComponent', () => {
  let component: PerformanceListItemComponent;
  let fixture: ComponentFixture<PerformanceListItemComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ PerformanceListItemComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(PerformanceListItemComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
