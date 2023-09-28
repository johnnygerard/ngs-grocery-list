import { HttpClient, HttpHeaders, HttpResponse, HttpStatusCode } from '@angular/common/http';
import { Product } from 'src/models/product.type';
import { ApiService, BASE_URL } from './api.service';
import { of } from 'rxjs';

describe('ApiService', () => {
  let service: ApiService;
  let httpClientMock: jasmine.SpyObj<HttpClient>;
  const initialGroceryList: Product[] = [
    { id: 1n, name: 'APPLES', quantity: 2 },
    { id: 2n, name: 'BANANAS', quantity: 3 },
  ];

  beforeEach(() => {
    httpClientMock = jasmine.createSpyObj('HttpClient', ['get', 'post', 'patch', 'delete']);
    httpClientMock.get.withArgs(BASE_URL).and.returnValue(of(initialGroceryList));
  });

  it('should instantiate and call getAllProducts', () => {
    // when
    service = new ApiService(httpClientMock);

    // then
    expect(service.groceryList).toBe(initialGroceryList);
    expect(httpClientMock.get).toHaveBeenCalledOnceWith(BASE_URL);
  });

  it('should call addProduct', () => {
    // given
    const body = { name: 'APPLES', quantity: 5 };
    const productId = 3n;
    const response: HttpResponse<void> = new HttpResponse({
      headers: new HttpHeaders({ Location: `${BASE_URL}/${productId}` }),
      status: HttpStatusCode.Created
    });
    httpClientMock.post.withArgs(BASE_URL, body).and.returnValue(of(response));
    service = new ApiService(httpClientMock);

    // when
    service.addProduct(body.name, body.quantity);

    // then
    expect(httpClientMock.post).toHaveBeenCalledOnceWith(BASE_URL, body);
    expect(service.groceryList.pop()).toEqual({ id: productId, ...body });
  });

  it('should call deleteAllProducts', () => {
    // given
    httpClientMock.delete.withArgs(BASE_URL).and.returnValue(of(null));
    service = new ApiService(httpClientMock);

    // when
    service.deleteAllProducts();

    // then
    expect(httpClientMock.delete).toHaveBeenCalledOnceWith(BASE_URL);
    expect(service.groceryList).toHaveSize(0);
  });

  it('should call updateQuantity', () => {
    // given
    const productIndex = 0;
    const product = initialGroceryList[productIndex];
    const quantity = product.quantity + 1;
    const response: HttpResponse<void> = new HttpResponse({
      status: HttpStatusCode.NoContent
    });
    const url = `${BASE_URL}/${product.id}?quantity=${quantity}`;
    httpClientMock.patch.withArgs(url, null).and.returnValue(of(response));
    service = new ApiService(httpClientMock);

    // when
    service.updateQuantity(product.id, quantity);

    // then
    expect(httpClientMock.patch).toHaveBeenCalledOnceWith(url, null);
    expect(service.groceryList[productIndex].quantity).toEqual(quantity);
  });

  it('should call deleteProduct', () => {
    // given
    const productId = initialGroceryList[0].id;
    const response: HttpResponse<void> = new HttpResponse({
      status: HttpStatusCode.NoContent
    });
    const url = `${BASE_URL}/${productId}`;
    httpClientMock.delete.withArgs(url).and.returnValue(of(response));
    service = new ApiService(httpClientMock);

    // when
    service.deleteProduct(productId);

    // then
    expect(httpClientMock.delete).toHaveBeenCalledOnceWith(url);
    expect(service.groceryList.find(product => product.id === productId)).toBeUndefined();
  });

  it('should call getAllProductNames', () => {
    // given
    const names = ['APPLES', 'BANANAS', 'MILK', 'BEEF'];
    httpClientMock.get.withArgs(`${BASE_URL}/names`).and.returnValue(of(names));
    service = new ApiService(httpClientMock);

    // when
    service.getAllProductNames().subscribe(result => {
      // then
      expect(httpClientMock.get).toHaveBeenCalledTimes(2); // 1st call in constructor
      expect(httpClientMock.get).toHaveBeenCalledWith(`${BASE_URL}/names`);
      expect(result).toEqual(names);
    });
  });
});
