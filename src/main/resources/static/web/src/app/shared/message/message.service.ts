import { Injectable } from '@angular/core';

@Injectable()
export class MessageService {
  message: string;
  success: boolean;

  private set(message: string, success: boolean) {
    this.message = message;
    this.success = success;
  }

  setAsError(message: string){
    this.set(message, false)
  }

   setAsSuccess(message: string){
      this.set(message, true);
   }

  clear() {
    this.message = null;
  }
}
