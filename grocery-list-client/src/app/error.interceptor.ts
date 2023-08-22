import { HttpErrorResponse, HttpInterceptorFn } from '@angular/common/http';
import { delay, of, retry, tap, throwError } from 'rxjs';

export const errorInterceptor: HttpInterceptorFn = (req, next) => {
  return next(req).pipe(
    retry({
      count: 3,
      delay(error: HttpErrorResponse, retryCount: number) {
        // Retry non-HTTP errors after 1ms, 10ms, 100ms
        if (error.status === 0) {
          console.error(`Retrying non-HTTP error (Attempt #${retryCount}):`, error);
          return of(true).pipe(delay(10 ** (retryCount - 1)));
        }

        if (error.status >= 400) {
          console.error(error);
          // Propagate user-friendly error message
          return throwError(() => error.error ?? Error('Unknown error'));
        }

        throw Error('Invalid HTTP status code'); // Should never happen
      }
    })
  );
};
