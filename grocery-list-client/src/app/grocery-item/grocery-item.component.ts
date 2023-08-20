import { Component, Input } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ApiService } from '../api.service';
import { GroceryItem } from 'src/models/grocery-item.type';
import { GroceryNamePipe } from '../grocery-name.pipe';

@Component({
  selector: 'app-grocery-item',
  standalone: true,
  imports: [CommonModule, GroceryNamePipe],
  templateUrl: './grocery-item.component.html',
  styleUrls: ['./grocery-item.component.scss']
})
export class GroceryItemComponent {
  @Input({ required: true }) item!: GroceryItem;

  constructor(private api: ApiService) {}

  deleteItem(): void {
    this.api.deleteGroceryItem(this.item.id);
  }
}
