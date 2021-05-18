import { ComponentFixture, TestBed } from '@angular/core/testing';

import { AddSectortypeComponent } from './add-sectortype.component';

describe('AddSectortypeComponent', () => {
  let component: AddSectortypeComponent;
  let fixture: ComponentFixture<AddSectortypeComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ AddSectortypeComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(AddSectortypeComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
