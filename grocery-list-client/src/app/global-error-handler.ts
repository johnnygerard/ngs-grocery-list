import { ErrorHandler, Injectable, NgZone } from "@angular/core";
import { MatSnackBar } from "@angular/material/snack-bar";

@Injectable({
  providedIn: 'root'
})
export class GlobalErrorHandler implements ErrorHandler {
  constructor(
    private _snackBar: MatSnackBar,
    private _zone: NgZone,
  ) { }

  handleError(error: unknown): void {
    let errorMessage = 'An unknown error occurred.';

    console.error(error);

    if (error) {
      if (typeof error === 'object' && 'message' in error && error.message)
        error = error.message;

      if (typeof error === 'string' && error !== 'No message available')
        errorMessage = error;
    }

    this._zone.run(() => {
      this._snackBar.open(errorMessage, 'Dismiss', {
        duration: 10_000,
        horizontalPosition: 'center',
        verticalPosition: 'top',
      });
    });
  }
}
