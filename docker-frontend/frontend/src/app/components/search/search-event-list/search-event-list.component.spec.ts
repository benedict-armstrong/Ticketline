import { ComponentFixture, TestBed } from '@angular/core/testing';

import { SearchEventListComponent } from './search-event-list.component';

describe('SearchEventListComponent', () => {
  let component: SearchEventListComponent;
  let fixture: ComponentFixture<SearchEventListComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ SearchEventListComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(SearchEventListComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
