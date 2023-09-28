import { HttpClient, HttpResponse } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { Product } from 'src/models/product.type';

export const BASE_URL = '/api/products';

@Injectable({
  providedIn: 'root'
})
export class ApiService {
  #groceryList: Product[] = [];

  constructor(private _http: HttpClient) {
    this.getAllProducts().subscribe(
      products => this.#groceryList = products
    );
  }

  get groceryList(): Product[] {
    return this.#groceryList;
  }

  getAllProducts(): Observable<Product[]> {
    return this._http.get<Product[]>(BASE_URL);
  }

  addProduct(name: string, quantity: number): void {
    this._http
      .post<HttpResponse<void>>(BASE_URL, { name, quantity })
      .subscribe(response => {
        const location = response.headers.get('Location');
        if (!location) throw Error('Location header is missing');
        const id = BigInt(location.substring(location.lastIndexOf('/') + 1));
        this.#groceryList.push({ id, name, quantity });
      });
  }

  getGroceryOptions(): Observable<string[]> {
    return this._http.get<string[]>(`${BASE_URL}/names`);
  }

  deleteGroceryList(): void {
    this._http
      .delete<void>(BASE_URL)
      .subscribe(() => this.#groceryList = []);
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
