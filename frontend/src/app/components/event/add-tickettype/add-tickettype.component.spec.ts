import { ComponentFixture, TestBed } from '@angular/core/testing';

import { AddTickettypeComponent } from './add-tickettype.component';

describe('AddTickettypeComponent', () => {
  let component: AddTickettypeComponent;
  let fixture: ComponentFixture<AddTickettypeComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ AddTickettypeComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(AddTickettypeComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
