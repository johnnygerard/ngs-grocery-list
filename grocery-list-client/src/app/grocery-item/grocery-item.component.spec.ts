import { ComponentFixture, TestBed } from '@angular/core/testing';

import { GroceryItemComponent } from './grocery-item.component';

describe('GroceryItemComponent', () => {
  let component: GroceryItemComponent;
  let fixture: ComponentFixture<GroceryItemComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [GroceryItemComponent]
    });
    fixture = TestBed.createComponent(GroceryItemComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
