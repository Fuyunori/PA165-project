import { Injectable } from '@angular/core';
import { ActivatedRouteSnapshot, CanActivate, Router } from '@angular/router';
import { Observable } from 'rxjs';
import { map, take, tap } from 'rxjs/operators';
import { AuthService } from '../services/auth.service';

type ValidationResult =
  | {
      canActivate: true;
    }
  | {
      canActivate: false;
      redirectTo: string;
    };

@Injectable({ providedIn: 'root' })
export class AuthGuard implements CanActivate {
  constructor(
    private readonly auth: AuthService,
    private readonly router: Router,
  ) {}

  canActivate(route: ActivatedRouteSnapshot): Observable<boolean> {
    return this.auth.loggedIn$.pipe(
      map((loggedIn): ValidationResult => {
        if (loggedIn && route.url[0].path === 'landing') {
          return { canActivate: false, redirectTo: '/main' };
        }

        if (!loggedIn && route.url[0].path === 'main') {
          return { canActivate: false, redirectTo: '/landing' };
        }

        return { canActivate: true };
      }),
      tap(result => {
        if (!result.canActivate) {
          this.router.navigateByUrl(result.redirectTo);
        }
      }),
      map(({ canActivate }) => canActivate),
      take(1),
    );
  }
}
