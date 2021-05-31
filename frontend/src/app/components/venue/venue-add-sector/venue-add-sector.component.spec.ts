import { ComponentFixture, TestBed } from '@angular/core/testing';

import { VenueAddSectorComponent } from './venue-add-sector.component';

describe('VenueAddSectorComponent', () => {
  let component: VenueAddSectorComponent;
  let fixture: ComponentFixture<VenueAddSectorComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ VenueAddSectorComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(VenueAddSectorComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
