import { ComponentFixture, TestBed } from '@angular/core/testing';

import { AddressListItemComponent } from './address-list-item.component';

describe('AddressListItemComponent', () => {
  let component: AddressListItemComponent;
  let fixture: ComponentFixture<AddressListItemComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ AddressListItemComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(AddressListItemComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
