import { ComponentFixture, TestBed } from '@angular/core/testing';

import { EditGroceryItemDialogComponent } from './edit-grocery-item-dialog.component';

describe('EditGroceryItemDialogComponent', () => {
  let component: EditGroceryItemDialogComponent;
  let fixture: ComponentFixture<EditGroceryItemDialogComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [EditGroceryItemDialogComponent]
    });
    fixture = TestBed.createComponent(EditGroceryItemDialogComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
