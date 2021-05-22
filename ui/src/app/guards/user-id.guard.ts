import { Injectable } from '@angular/core';
import { AuthService } from '../services/auth.service';
import { Observable } from 'rxjs';
import { ActivatedRouteSnapshot, CanActivate, Router } from '@angular/router';
import { map, take, tap } from 'rxjs/operators';
import { NotificationService } from '../services/notification.service';
import { UserRole } from '../models/user.model';

@Injectable({ providedIn: 'root' })
export class UserIdGuard implements CanActivate {
  constructor(
    private readonly auth: AuthService,
    private readonly router: Router,
    private readonly notification: NotificationService,
  ) {}

  canActivate(route: ActivatedRouteSnapshot): Observable<boolean> {
    const routeId = Number(route.params.id);
    return this.auth.parsedToken$.pipe(
      map(
        parsedToken =>
          parsedToken != null &&
          (parsedToken.userId === routeId ||
            parsedToken.role === UserRole.Manager),
      ),
      tap(hasAccess => {
        if (!hasAccess) {
          this.notification.toastError(
            'Cannot access profiles of other users!',
          );
          this.router.navigateByUrl('/main');
        }
      }),
      take(1),
    );
  }
}
