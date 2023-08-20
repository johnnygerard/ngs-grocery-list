import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ApiService } from '../api.service';
import { GroceryItemComponent } from '../grocery-item/grocery-item.component';
import { GroceryItem } from 'src/models/grocery-item.type';

@Component({
  selector: 'app-grocery-list',
  standalone: true,
  imports: [CommonModule, GroceryItemComponent],
  templateUrl: './grocery-list.component.html',
  styleUrls: ['./grocery-list.component.scss']
})
export class GroceryListComponent {
  constructor(private api: ApiService) { }

  get groceryList(): GroceryItem[] {
    return this.api.groceryList;
  }

  get isGroceryListEmpty(): boolean {
    return this.groceryList.length === 0;
  }

  clearList(): void {
    this.api.deleteGroceryList();
  }
}
