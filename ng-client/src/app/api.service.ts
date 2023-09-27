import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { Product } from 'src/models/product.type';

const BASE_URL = '/api/products';

@Injectable({
  providedIn: 'root'
})
export class ApiService {
  #groceryList: Product[] = [];

  constructor(private _http: HttpClient) {
    this._http.get<Product[]>(BASE_URL).subscribe(
      list => this.#groceryList = list
    );
  }

  get groceryList(): Product[] {
    return this.#groceryList;
  }

  getGroceryOptions(): Observable<string[]> {
    return this._http.get<string[]>(`${BASE_URL}/names`);
  }

  deleteGroceryList(): void {
    this._http
      .delete<void>(BASE_URL)
      .subscribe(() => this.#groceryList = []);
  }

  addProduct(name: string, quantity: number): void {
    this._http
      .post<bigint>(`${BASE_URL}/${name}?q=${quantity}`, null)
      .subscribe(id => this.#groceryList.push({ id, name, quantity }));
  }

  deleteProduct(id: bigint): void {
    this._http
      .delete<void>(`${BASE_URL}/${id}`)
      .subscribe(() => {
        const index = this.#findProduct(id);

        this.#groceryList.splice(index, 1);
      });
  }

  updateProductQuantity(id: bigint, quantity: number): void {
    this._http
      .patch<Product>(`${BASE_URL}/${id}?q=${quantity}`, null)
      .subscribe(() => {
        const index = this.#findProduct(id);

        this.#groceryList[index].quantity = quantity;
      });
  }

  #findProduct(id: bigint): number {
    const index = this.#groceryList.findIndex(item => item.id === id);

    if (index < 0) throw Error(`Product with id ${id} not found`);
    return index;
  }
}
