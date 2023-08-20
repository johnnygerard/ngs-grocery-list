import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { GroceryItem } from 'src/models/grocery-item.type';

@Injectable({
  providedIn: 'root'
})
export class ApiService {
  #groceryList: GroceryItem[] = [];

  constructor(private http: HttpClient) {
    this.http.get<GroceryItem[]>('/api/grocery-list').subscribe(
      list => this.#groceryList = list
    );
  }

  get groceryList(): GroceryItem[] {
    return this.#groceryList;
  }

  getGroceryOptions() {
    return this.http.get<string[]>('/api/grocery-options');
  }

  deleteGroceryList(): void {
    this.http
      .delete<void>('/api/grocery-list')
      .subscribe(() => this.#groceryList = []);
  }

  addGroceryItem(name: string, quantity: number): void {
    this.http
      .post<GroceryItem>(`/api/grocery-item/${name}?q=${quantity}`, null)
      .subscribe(item => this.#groceryList.push(item));
  }

  deleteGroceryItem(id: bigint) {
    return this.http.delete<void>(`/api/grocery-item/${id}`);
  }

  updateGroceryItemQuantity(id: bigint, quantity: number) {
    return this.http.patch<GroceryItem>(`/api/grocery-item/${id}?q=${quantity}`, null);
  }
}
