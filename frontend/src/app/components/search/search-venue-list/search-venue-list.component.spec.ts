import { ComponentFixture, TestBed } from '@angular/core/testing';

import { SearchVenueListComponent } from './search-venue-list.component';

describe('SearchVenueListComponent', () => {
  let component: SearchVenueListComponent;
  let fixture: ComponentFixture<SearchVenueListComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ SearchVenueListComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(SearchVenueListComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
