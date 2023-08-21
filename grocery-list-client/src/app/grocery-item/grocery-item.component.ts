import { Component, Input } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ApiService } from '../api.service';
import { GroceryItem } from 'src/models/grocery-item.type';
import { GroceryNamePipe } from '../grocery-name.pipe';
import { MatIconModule } from '@angular/material/icon';
import { MatCardModule } from '@angular/material/card';
import { MatDialog } from '@angular/material/dialog';
import { EditGroceryItemDialogComponent } from '../edit-grocery-item-dialog/edit-grocery-item-dialog.component';

@Component({
  selector: 'app-grocery-item',
  standalone: true,
  imports: [
    CommonModule,
    GroceryNamePipe,
    MatIconModule,
    MatCardModule,
  ],
  templateUrl: './grocery-item.component.html',
  styleUrls: ['./grocery-item.component.scss']
})
export class GroceryItemComponent {
  @Input({ required: true }) item!: GroceryItem;

  constructor(
    private api: ApiService,
    private dialog: MatDialog
  ) { }

  deleteItem(): void {
    this.api.deleteGroceryItem(this.item.id);
  }

  openEditDialog() {
    const dialogRef = this.dialog.open(EditGroceryItemDialogComponent, {
      data: { ...this.item }
    });

    dialogRef.afterClosed().subscribe(quantity => {
      if (typeof quantity === 'number')
        this.api.updateGroceryItemQuantity(this.item.id, quantity);
    });
  }
}
