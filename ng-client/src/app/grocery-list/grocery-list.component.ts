import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ApiService } from '../api.service';
import { GroceryItemComponent } from '../grocery-item/grocery-item.component';
import { GroceryItem } from 'src/models/grocery-item.type';
import { MatButtonModule } from '@angular/material/button';
import { MatDialog, MatDialogModule } from '@angular/material/dialog';
import { ClearListDialogComponent } from '../clear-list-dialog/clear-list-dialog.component';

@Component({
  selector: 'app-grocery-list',
  standalone: true,
  imports: [
    CommonModule,
    GroceryItemComponent,
    MatButtonModule,
    MatDialogModule,
  ],
  templateUrl: './grocery-list.component.html',
  styleUrls: ['./grocery-list.component.scss']
})
export class GroceryListComponent {
  constructor(
    private api: ApiService,
    private dialog: MatDialog,
  ) { }

  get groceryList(): GroceryItem[] {
    return this.api.groceryList;
  }

  get isGroceryListEmpty(): boolean {
    return this.groceryList.length === 0;
  }

  openClearListDialog(): void {
    const dialogRef = this.dialog.open(ClearListDialogComponent);

    dialogRef.afterClosed().subscribe((result: boolean) => {
      if (result)
        this.api.deleteGroceryList();
    });
  }
}
