import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { GroceryItem } from 'src/models/grocery-item.type';

const BASE_URL = '/api/grocery-items';

@Injectable({
  providedIn: 'root'
})
export class ApiService {
  #groceryList: GroceryItem[] = [];

  constructor(private _http: HttpClient) {
    this._http.get<GroceryItem[]>(BASE_URL).subscribe(
      list => this.#groceryList = list
    );
  }

  get groceryList(): GroceryItem[] {
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

  addGroceryItem(name: string, quantity: number): void {
    this._http
      .post<bigint>(`${BASE_URL}/${name}?q=${quantity}`, null)
      .subscribe(id => this.#groceryList.push({ id, name, quantity }));
  }

  deleteGroceryItem(id: bigint): void {
    this._http
      .delete<void>(`${BASE_URL}/${id}`)
      .subscribe(() => {
        const index = this.#findGroceryItem(id);

        this.#groceryList.splice(index, 1);
      });
  }

  updateGroceryItemQuantity(id: bigint, quantity: number): void {
    this._http
      .patch<GroceryItem>(`${BASE_URL}/${id}?q=${quantity}`, null)
      .subscribe(() => {
        const index = this.#findGroceryItem(id);

        this.#groceryList[index].quantity = quantity;
      });
  }

  #findGroceryItem(id: bigint): number {
    const index = this.#groceryList.findIndex(item => item.id === id);

    if (index < 0) throw Error(`Grocery item with id ${id} not found`);
    return index;
  }
}
