import {async} from "@angular/core/testing";
import {CustomValidators} from "./CustomValidators";

describe('CustomerValidators', () => {

  it('with the min and max return no error', async(() => {
    expect(CustomValidators.validateNumber(200, 0, 201)).toBeNull();
  }));

  it('expect return invalid max value', async(() => {
    expect(CustomValidators.validateNumber(200, 0, 190).number).toBe(true)
  }));

  it('expect return invalid max value when min not supplied', async(() => {
    expect(CustomValidators.validateNumber(200, -1, 190).number).toBe(true)
  }));

  it('expect return value min value', async(() => {
    expect(CustomValidators.validateNumber(0.5, 0.4, 0.6)).toBeNull()
  }));

  it('expect return value min value', async(() => {
    expect(CustomValidators.validateNumber(0.000000000009, 0, 1)).toBeNull()
  }));

  it('expect NAN', async(() => {
    expect(CustomValidators.validateNumber(NaN, 0, 1).number).toBe(true)
  }));
});
