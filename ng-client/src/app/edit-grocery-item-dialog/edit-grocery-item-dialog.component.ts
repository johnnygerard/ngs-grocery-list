import { Component, Inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { MAT_DIALOG_DATA, MatDialogModule } from '@angular/material/dialog';
import { MatButtonModule } from '@angular/material/button';
import { MatFormFieldModule } from '@angular/material/form-field';
import { FormsModule } from '@angular/forms';
import { GroceryItem } from 'src/models/grocery-item.type';
import { MatInputModule } from '@angular/material/input';
import { GroceryNamePipe } from '../grocery-name.pipe';

@Component({
  selector: 'app-edit-grocery-item-dialog',
  standalone: true,
  imports: [
    CommonModule,
    FormsModule,
    MatDialogModule,
    MatButtonModule,
    MatFormFieldModule,
    MatInputModule,
    GroceryNamePipe,
  ],
  templateUrl: './edit-grocery-item-dialog.component.html',
  styleUrls: ['./edit-grocery-item-dialog.component.scss']
})
export class EditGroceryItemDialogComponent {
  initialQuantity = this.data.quantity;
  quantity = this.data.quantity;

  constructor(@Inject(MAT_DIALOG_DATA) public data: GroceryItem) { }
}
