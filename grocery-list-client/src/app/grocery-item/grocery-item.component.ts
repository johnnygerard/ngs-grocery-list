import { Component, Input } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ApiService } from '../api.service';
import { GroceryItem } from 'src/models/grocery-item.type';
import { GroceryNamePipe } from '../grocery-name.pipe';
import { FormsModule } from '@angular/forms';
import {MatIconModule} from '@angular/material/icon';

@Component({
  selector: 'app-grocery-item',
  standalone: true,
  imports: [
    CommonModule,
    GroceryNamePipe,
    FormsModule,
    MatIconModule,
  ],
  templateUrl: './grocery-item.component.html',
  styleUrls: ['./grocery-item.component.scss']
})
export class GroceryItemComponent {
  @Input({ required: true }) item!: GroceryItem;
  quantity = 1;

  constructor(private api: ApiService) { }

  deleteItem(): void {
    this.api.deleteGroceryItem(this.item.id);
  }

  updateQuantity(): void {
    this.api.updateGroceryItemQuantity(this.item.id, this.quantity);
  }
}
