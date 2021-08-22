import { ComponentFixture, TestBed } from '@angular/core/testing';

import { VenueSectorListComponent } from './venue-sector-list.component';

describe('VenueSectorListComponent', () => {
  let component: VenueSectorListComponent;
  let fixture: ComponentFixture<VenueSectorListComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ VenueSectorListComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(VenueSectorListComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
