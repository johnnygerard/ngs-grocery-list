import { ErrorHandler } from "@angular/core";

export class GlobalErrorHandler implements ErrorHandler {
  handleError(error: Error): void {
    window.alert('An error occurred: \n\n' + error.message);
  }
}
