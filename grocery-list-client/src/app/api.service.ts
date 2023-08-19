import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { GroceryItem } from 'src/models/grocery-item.type';

@Injectable({
  providedIn: 'root'
})
export class ApiService {
  constructor(private http: HttpClient) { }

  getGroceryList() {
    return this.http.get<GroceryItem[]>('/api/grocery-list');
  }

  getGroceryOptions() {
    return this.http.get<string[]>('/api/grocery-options');
  }

  deleteGroceryList() {
    return this.http.delete<void>('/api/grocery-list');
  }

  addGroceryItem(name: string, quantity: number) {
    return this.http.post<GroceryItem>(`/api/grocery-item/${name}`, {
      params: { q: quantity }
    });
  }

  deleteGroceryItem(id: bigint) {
    return this.http.delete<void>(`/api/grocery-item/${id}`);
  }

  updateGroceryItemQuantity(id: bigint, quantity: number) {
    return this.http.patch<GroceryItem>(`/api/grocery-item/${id}`, {
      params: { q: quantity }
    });
  }
}
