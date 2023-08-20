import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ApiService } from '../api.service';
import { GroceryNamePipe } from '../grocery-name.pipe';
import { FormsModule } from '@angular/forms';

@Component({
  selector: 'app-add-grocery-item',
  standalone: true,
  imports: [
    CommonModule,
    GroceryNamePipe,
    FormsModule,
  ],
  templateUrl: './add-grocery-item.component.html',
  styleUrls: ['./add-grocery-item.component.scss']
})
export class AddGroceryItemComponent {
  options$ = this.api.getGroceryOptions();
  selectedOption = '';
  quantity = 1;

  constructor(private api: ApiService) { }

  addGroceryItem(): void {
    this.api.addGroceryItem(this.selectedOption, this.quantity).subscribe();
    this.selectedOption = '';
    this.quantity = 1;
  }
}
