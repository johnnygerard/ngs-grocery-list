import { Component, Input } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ApiService } from '../api.service';
import { Product } from 'src/models/product.type';
import { ProductNamePipe } from '../product-name.pipe';
import { MatIconModule } from '@angular/material/icon';
import { MatCardModule } from '@angular/material/card';
import { MatDialog } from '@angular/material/dialog';
import { EditProductDialogComponent } from '../edit-product-dialog/edit-product-dialog.component';

@Component({
  selector: 'app-product',
  standalone: true,
  imports: [
    CommonModule,
    ProductNamePipe,
    MatIconModule,
    MatCardModule,
  ],
  templateUrl: './product.component.html',
  styleUrls: ['./product.component.scss']
})
export class ProductComponent {
  @Input({ required: true }) product!: Product;

  constructor(
    private api: ApiService,
    private dialog: MatDialog
  ) { }

  deleteProduct(): void {
    this.api.deleteProduct(this.product.id);
  }

  openEditDialog() {
    const dialogRef = this.dialog.open(EditProductDialogComponent, {
      data: { ...this.product }
    });

    dialogRef.afterClosed().subscribe(quantity => {
      if (typeof quantity === 'number')
        this.api.updateQuantity(this.product.id, quantity);
    });
  }
}
