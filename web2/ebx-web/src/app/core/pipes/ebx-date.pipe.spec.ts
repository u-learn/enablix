import { EbxDatePipe } from './ebx-date.pipe';

describe('DefaultDatePipe', () => {
  it('create an instance', () => {
    const pipe = new EbxDatePipe();
    expect(pipe).toBeTruthy();
  });
});
