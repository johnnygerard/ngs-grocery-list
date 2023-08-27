import { HttpErrorResponse, HttpInterceptorFn } from '@angular/common/http';
import { delay, of, retry, tap, throwError } from 'rxjs';

export const errorInterceptor: HttpInterceptorFn = (req, next) => {
  return next(req).pipe(
    retry({
      count: 3,
      delay(error: HttpErrorResponse, retryCount: number) {
        // Retry non-HTTP errors after 1ms, 10ms, 100ms
        if (error.status === 0) {
          console.error(`Retry failed request (Attempt #${retryCount})...`, error);
          return of(true).pipe(delay(10 ** (retryCount - 1)));
        }

        // Propagate user-friendly error message
        if (error.status >= 400)
          return throwError(() => error.error ?? Error('Unknown error'));

        throw Error('Invalid HTTP status code'); // Should never happen
      }
    })
  );
};
