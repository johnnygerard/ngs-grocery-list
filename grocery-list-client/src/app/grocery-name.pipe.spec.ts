import { GroceryNamePipe } from './grocery-name.pipe';

describe('GroceryNamePipe', () => {
  it('should create an instance', () => {
    const pipe = new GroceryNamePipe();
    expect(pipe).toBeTruthy();
  });

  it('should transform "BANANAS" to "Bananas"', () => {
    const pipe = new GroceryNamePipe();
    expect(pipe.transform('BANANA')).toBe('Banana');
  });

  it('should transform "PEANUT_BUTTER" to "Peanut Butter"', () => {
    const pipe = new GroceryNamePipe();
    expect(pipe.transform('PEANUT_BUTTER')).toBe('Peanut Butter');
  });
});
