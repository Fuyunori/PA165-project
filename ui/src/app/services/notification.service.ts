import { Injectable } from '@angular/core';
import { ToastrService } from 'ngx-toastr';
import { EMPTY, OperatorFunction } from 'rxjs';
import { catchError } from 'rxjs/operators';

@Injectable({ providedIn: 'root' })
export class NotificationService {
  constructor(private readonly toastr: ToastrService) {}

  toastError(message: string): void {
    this.toastr.error(message);
  }

  toastSuccess(message: string): void {
    this.toastr.success(message);
  }

  onError<T>(message: string): OperatorFunction<T, T> {
    return $ =>
      $.pipe(
        catchError(error => {
          console.warn(error);
          this.toastError(message);
          return EMPTY;
        }),
      );
  }
}
