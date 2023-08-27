import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { AddGroceryItemComponent } from './add-grocery-item/add-grocery-item.component';
import { GroceryListComponent } from './grocery-list/grocery-list.component';

@Component({
  selector: 'app-root',
  standalone: true,
  imports: [
    CommonModule,
    AddGroceryItemComponent,
    GroceryListComponent,
  ],
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.scss']
})
export class AppComponent {
  title = 'grocery-list-client';
}
