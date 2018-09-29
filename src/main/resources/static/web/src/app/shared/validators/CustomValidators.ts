import {FormControl, ValidatorFn, Validators} from "@angular/forms";

export class CustomValidators {

  static number(options = {}): ValidatorFn {
    return (control: FormControl): any => {

      if (Validators.required(control) != null) {
        return null;
      }

      let val: number = control.value;
      let min = options['min'];
      let max = options['max'];

      return this.validateNumber(val, min, max);
    };
  }

  private static validateNumber(val: number, min: number, max: number) {

    if (isNaN(val) || !(/\d*\.?\d*/.test(val.toString()))) {
      return {"number": true};
    } else if (!isNaN(min) && !isNaN(max)) {
      return val <= min || val >= max ? {"number": true} : null;
    } else if (!isNaN(min)) {
      return val <= min ? {"number": true} : null;
    } else if (!isNaN(max)) {
      return val >= max ? {"number": true} : null;
    } else {
      return null;
    }
  }

}
