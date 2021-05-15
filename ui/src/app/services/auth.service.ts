import { Injectable } from '@angular/core';
import { BehaviorSubject, Observable, of } from 'rxjs';
import { map } from 'rxjs/operators';

type AuthState =
  | {
      status: 'loggedIn';
      token: string;
    }
  | {
      status: 'loggedOut';
    }
  | {
      status: 'loginFailed';
    };

@Injectable({ providedIn: 'root' })
export class AuthService {
  private readonly state$ = new BehaviorSubject<AuthState>({
    status: 'loggedOut',
  });

  readonly loggedIn$: Observable<boolean> = this.state$.pipe(
    map(({ status }) => status === 'loggedIn'),
  );

  readonly loginFailed$: Observable<boolean> = this.state$.pipe(
    map(({ status }) => status === 'loginFailed'),
  );

  // TODO mocked for now
  readonly userIsManager$: Observable<boolean> = of(true);

  logIn(username: string, password: string): void {
    // TODO the authentication endpoint will be called here, now mocked

    setTimeout(() => {
      this.state$.next(
        username === 'root' && password === 'toor'
          ? { status: 'loggedIn', token: 'xyz123' }
          : { status: 'loginFailed' },
      );
    }, 1000);
  }

  logOut(): void {
    this.state$.next({ status: 'loggedOut' });
  }
}
