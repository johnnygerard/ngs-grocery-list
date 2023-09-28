import { ProductNamePipe } from './product-name.pipe';

describe('ProductNamePipe', () => {
  it('should create an instance', () => {
    const pipe = new ProductNamePipe();
    expect(pipe).toBeTruthy();
  });

  it('should transform "BANANAS" to "Bananas"', () => {
    const pipe = new ProductNamePipe();
    expect(pipe.transform('BANANA')).toBe('Banana');
  });

  it('should transform "PEANUT_BUTTER" to "Peanut Butter"', () => {
    const pipe = new ProductNamePipe();
    expect(pipe.transform('PEANUT_BUTTER')).toBe('Peanut Butter');
  });
});
