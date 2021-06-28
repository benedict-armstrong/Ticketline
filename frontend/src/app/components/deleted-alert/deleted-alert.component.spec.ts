import { ComponentFixture, TestBed } from '@angular/core/testing';

import { DeletedAlertComponent } from './deleted-alert.component';

describe('DeletedAlertComponent', () => {
  let component: DeletedAlertComponent;
  let fixture: ComponentFixture<DeletedAlertComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ DeletedAlertComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(DeletedAlertComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
