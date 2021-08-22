import { ComponentFixture, TestBed } from '@angular/core/testing';

import { TicketOrderItemComponent } from './ticket-order-item.component';

describe('NewsItemComponent', () => {
  let component: TicketOrderItemComponent;
  let fixture: ComponentFixture<TicketOrderItemComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ TicketOrderItemComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(TicketOrderItemComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
