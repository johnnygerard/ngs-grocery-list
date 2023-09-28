import { Pipe, PipeTransform } from '@angular/core';

@Pipe({
  name: 'productName',
  standalone: true
})
export class ProductNamePipe implements PipeTransform {

  // Example: transform('PEANUT_BUTTER') => 'Peanut Butter'
  transform(value: string): string {
    return value.toLowerCase().split('_').map(
      word => word[0].toUpperCase() + word.substring(1)
    ).join(' ');
  }

}
