import { ComponentFixture, TestBed } from '@angular/core/testing';

import { AddGroceryItemComponent } from './add-grocery-item.component';

describe('AddGroceryItemComponent', () => {
  let component: AddGroceryItemComponent;
  let fixture: ComponentFixture<AddGroceryItemComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [AddGroceryItemComponent]
    });
    fixture = TestBed.createComponent(AddGroceryItemComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
