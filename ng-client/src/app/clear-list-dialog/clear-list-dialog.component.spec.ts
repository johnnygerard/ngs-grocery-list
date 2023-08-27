import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ClearListDialogComponent } from './clear-list-dialog.component';

describe('ClearListDialogComponent', () => {
  let component: ClearListDialogComponent;
  let fixture: ComponentFixture<ClearListDialogComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [ClearListDialogComponent]
    });
    fixture = TestBed.createComponent(ClearListDialogComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
