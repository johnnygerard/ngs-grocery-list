import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { AddGroceryItemComponent } from './add-grocery-item/add-grocery-item.component';
import { GroceryListComponent } from './grocery-list/grocery-list.component';
import { MatIconRegistry } from '@angular/material/icon';
import { DomSanitizer } from '@angular/platform-browser';
import { deleteIcon, editIcon } from './svg-icons';

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
export class AppComponent implements OnInit {
  constructor(
    private _registry: MatIconRegistry,
    private _sanitizer: DomSanitizer,
  ) { }

  ngOnInit() {
    this.#registerIcons();
  }

  #registerIcons(): void {
    this._registry.addSvgIconLiteral(
      'edit',
      this._sanitizer.bypassSecurityTrustHtml(editIcon)
    )

    this._registry.addSvgIconLiteral(
      'delete',
      this._sanitizer.bypassSecurityTrustHtml(deleteIcon)
    );
  }
}
