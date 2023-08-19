import { Component, Input } from '@angular/core';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-grocery-item',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './grocery-item.component.html',
  styleUrls: ['./grocery-item.component.scss']
})
export class GroceryItemComponent {
  @Input({ required: true }) name = '';
  @Input({ required: true }) quantity = 0;
}
