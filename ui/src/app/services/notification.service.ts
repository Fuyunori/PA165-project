import { Injectable } from '@angular/core';
import { ToastrService } from 'ngx-toastr';
import { EMPTY, OperatorFunction } from 'rxjs';
import { catchError } from 'rxjs/operators';

@Injectable({ providedIn: 'root' })
export class NotificationService {
  constructor(private readonly toastr: ToastrService) {}

  onError<T>(message: string): OperatorFunction<T, T> {
    return $ =>
      $.pipe(
        catchError(error => {
          console.warn(error);
          this.toastr.error(message);
          return EMPTY;
        }),
      );
  }
}
