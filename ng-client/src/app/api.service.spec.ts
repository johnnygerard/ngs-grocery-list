import { HttpClient } from '@angular/common/http';
import { Product } from 'src/models/product.type';
import { ApiService, BASE_URL } from './api.service';
import { of } from 'rxjs';

describe('ApiService', () => {
  let service: ApiService;
  let httpClientMock: jasmine.SpyObj<HttpClient>;

  beforeEach(() => {
    httpClientMock = jasmine.createSpyObj('HttpClient', ['get']);
  });

  it('should instantiate and call getAllProducts', () => {
    const products: Product[] = [
      { id: 1n, name: 'APPLES', quantity: 2 },
      { id: 2n, name: 'BANANAS', quantity: 3 },
    ];
    httpClientMock.get.withArgs(BASE_URL).and.returnValue(of(products));
    service = new ApiService(httpClientMock);

    expect(service.groceryList).toBe(products);
    expect(httpClientMock.get).toHaveBeenCalledWith(BASE_URL);
    expect(httpClientMock.get).toHaveBeenCalledTimes(1);
  });
});
