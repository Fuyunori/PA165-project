import { CanActivate, Router } from '@angular/router';
import { Observable } from 'rxjs';
import { AuthService } from '../services/auth.service';
import { take, tap } from 'rxjs/operators';
import { Injectable } from '@angular/core';
import { NotificationService } from '../services/notification.service';

@Injectable({ providedIn: 'root' })
export class ManagerGuard implements CanActivate {
  constructor(
    private readonly auth: AuthService,
    private readonly router: Router,
    private readonly notification: NotificationService,
  ) {}

  canActivate(): Observable<boolean> {
    return this.auth.userIsManager$.pipe(
      tap(userIsManager => {
        if (!userIsManager) {
          this.notification.toastError('Only managers can access this view!');
          this.router.navigateByUrl('/main');
        }
      }),
      take(1),
    );
  }
}
