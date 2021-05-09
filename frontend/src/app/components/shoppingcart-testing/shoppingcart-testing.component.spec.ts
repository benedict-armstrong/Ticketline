import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ShoppingcartTestingComponent } from './shoppingcart-testing.component';

describe('ShoppingcartTestingComponent', () => {
  let component: ShoppingcartTestingComponent;
  let fixture: ComponentFixture<ShoppingcartTestingComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ ShoppingcartTestingComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(ShoppingcartTestingComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
