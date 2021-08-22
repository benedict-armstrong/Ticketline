import { ComponentFixture, TestBed } from '@angular/core/testing';

import { SelectSeatUnitComponent } from './select-seat-unit.component';

describe('SelectSeatUnitComponent', () => {
  let component: SelectSeatUnitComponent;
  let fixture: ComponentFixture<SelectSeatUnitComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ SelectSeatUnitComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(SelectSeatUnitComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
