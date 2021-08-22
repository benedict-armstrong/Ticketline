import { ComponentFixture, TestBed } from '@angular/core/testing';

import { VenueLayoutGridComponent } from './venue-layout-grid.component';

describe('VenueLayoutGridComponent', () => {
  let component: VenueLayoutGridComponent;
  let fixture: ComponentFixture<VenueLayoutGridComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ VenueLayoutGridComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(VenueLayoutGridComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
