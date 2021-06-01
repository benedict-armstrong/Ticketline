import { ComponentFixture, TestBed } from '@angular/core/testing';

import { SelectVenueComponent } from './select-venue.component';

describe('SelectVenueComponent', () => {
  let component: SelectVenueComponent;
  let fixture: ComponentFixture<SelectVenueComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ SelectVenueComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(SelectVenueComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
