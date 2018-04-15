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

      if (isNaN(val) || !(/\d*\.?\d*/.test(val.toString()))) {
        return {"number": "isNan"};
      } else if (!isNaN(min) && !isNaN(max)) {
        return val <= min || val >= max ? {"number": "min-max"} : null;
      } else if (!isNaN(min)) {
        return val <= min ? {"number": "min"} : null;
      } else if (!isNaN(max)) {
        return val >= max ? {"number": "max"} : null;
      } else {
        return null;
      }
    };
  }

}
