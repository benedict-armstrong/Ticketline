import { ComponentFixture, TestBed } from '@angular/core/testing';

import { VenueGridUnitComponent } from './venue-grid-unit.component';

describe('VenueGridUnitComponent', () => {
  let component: VenueGridUnitComponent;
  let fixture: ComponentFixture<VenueGridUnitComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ VenueGridUnitComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(VenueGridUnitComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
