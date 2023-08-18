import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { MatSlideToggleModule } from '@angular/material/slide-toggle'

@Component({
  selector: 'app-root',
  standalone: true,
  imports: [CommonModule, MatSlideToggleModule],
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.scss']
})
export class AppComponent {
  title = 'grocery-list-client';
}
