import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { AddGroceryItemComponent } from './add-grocery-item/add-grocery-item.component';

@Component({
  selector: 'app-root',
  standalone: true,
  imports: [CommonModule, AddGroceryItemComponent],
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.scss']
})
export class AppComponent {
  title = 'grocery-list-client';
}
