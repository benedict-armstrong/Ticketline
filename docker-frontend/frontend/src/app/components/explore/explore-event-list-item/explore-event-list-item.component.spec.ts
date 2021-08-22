import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ExploreEventListItemComponent } from './explore-event-list-item.component';

describe('ExploreEventListItemComponent', () => {
  let component: ExploreEventListItemComponent;
  let fixture: ComponentFixture<ExploreEventListItemComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ ExploreEventListItemComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(ExploreEventListItemComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
