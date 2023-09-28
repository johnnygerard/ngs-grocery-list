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
    httpClientMock = jasmine.createSpyObj('HttpClient', ['get', 'post']);
    httpClientMock.get.withArgs(BASE_URL).and.returnValue(of(initialGroceryList));
  });

  it('should instantiate and call getAllProducts', () => {
    // when
    service = new ApiService(httpClientMock);

    // then
    expect(service.groceryList).toBe(initialGroceryList);
    expect(httpClientMock.get).toHaveBeenCalledWith(BASE_URL);
    expect(httpClientMock.get).toHaveBeenCalledTimes(1);
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
    expect(httpClientMock.post).toHaveBeenCalledTimes(1);
    expect(httpClientMock.post).toHaveBeenCalledWith(BASE_URL, body);
    expect(service.groceryList.pop()).toEqual({ id: productId, ...body });
  });
});
