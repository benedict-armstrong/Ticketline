import { ComponentFixture, TestBed } from '@angular/core/testing';

import { BannedAlertComponent } from './banned-alert.component';

describe('BannedAlertComponent', () => {
  let component: BannedAlertComponent;
  let fixture: ComponentFixture<BannedAlertComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ BannedAlertComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(BannedAlertComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
