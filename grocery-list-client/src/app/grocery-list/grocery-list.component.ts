import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { GroceryItem } from 'src/models/grocery-item.type';
import { ApiService } from '../api.service';
import { GroceryItemComponent } from '../grocery-item/grocery-item.component';

@Component({
  selector: 'app-grocery-list',
  standalone: true,
  imports: [CommonModule, GroceryItemComponent],
  templateUrl: './grocery-list.component.html',
  styleUrls: ['./grocery-list.component.scss']
})
export class GroceryListComponent {
  groceryList: GroceryItem[] = [];

  constructor(private api: ApiService) {
    this.api.getGroceryList().subscribe(
      list => this.groceryList = list
    );
  }
}
