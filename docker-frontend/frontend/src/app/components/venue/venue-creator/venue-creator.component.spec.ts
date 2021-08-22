import { ComponentFixture, TestBed } from '@angular/core/testing';

import { VenueCreatorComponent } from './venue-creator.component';

describe('VenueCreatorComponent', () => {
  let component: VenueCreatorComponent;
  let fixture: ComponentFixture<VenueCreatorComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ VenueCreatorComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(VenueCreatorComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
