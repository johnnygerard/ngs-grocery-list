import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ApiService } from '../api.service';
import { GroceryNamePipe } from '../grocery-name.pipe';
import { FormsModule } from '@angular/forms';
import {MatFormFieldModule} from '@angular/material/form-field';
import {MatSelectModule} from '@angular/material/select';
import {MatInputModule} from '@angular/material/input';

@Component({
  selector: 'app-add-grocery-item',
  standalone: true,
  imports: [
    CommonModule,
    GroceryNamePipe,
    FormsModule,
    MatFormFieldModule,
    MatSelectModule,
    MatInputModule,
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
    this.api.addGroceryItem(this.selectedOption, this.quantity);
  }
}
