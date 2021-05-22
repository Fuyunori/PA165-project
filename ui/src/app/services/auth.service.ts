import { HttpClient, HttpErrorResponse } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { BehaviorSubject, Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import { environment } from 'src/environments/environment';
import { AuthInfo } from '../models/auth-info.model';
import { isJwtPayload, JwtPayload } from '../models/jwt-payload.model';
import { UserRole } from '../models/user.model';
import { StorageService } from './storage.service';

const RESOURCE_URL = `${environment.apiBaseUrl}/users`;

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

  readonly token$: Observable<string | null> = this.state$.pipe(
    map(state => (state.status === 'loggedIn' ? state.token : null)),
  );

  readonly parsedToken$: Observable<JwtPayload | null> = this.token$.pipe(
    map(token => (token != null ? AuthService.parseToken(token) : null)),
  );

  readonly userId$: Observable<number | null> = this.parsedToken$.pipe(
    map(parsedToken => parsedToken?.userId ?? null),
  );

  readonly userIsManager$: Observable<boolean> = this.parsedToken$.pipe(
    map(parsedToken => parsedToken?.role === UserRole.Manager),
  );

  constructor(
    private readonly http: HttpClient,
    private readonly storage: StorageService,
  ) {
    const { token } = this.storage;
    if (token != null) {
      this.state$.next({ status: 'loggedIn', token });
    }
  }

  logIn(authInfo: AuthInfo): void {
    this.http
      .post(`${RESOURCE_URL}/login`, authInfo, { responseType: 'text' })
      .subscribe(
        token => {
          this.state$.next({ status: 'loggedIn', token });
          this.storage.token = token;
        },
        (error: HttpErrorResponse) => {
          console.error(error);
          this.state$.next({ status: 'loginFailed' });
        },
      );
  }

  logOut(): void {
    this.state$.next({ status: 'loggedOut' });
    this.storage.token = null;
  }

  private static parseToken(token: string): JwtPayload | null {
    try {
      const payload = JSON.parse(atob(token.split('.')[1]));
      if (!isJwtPayload(payload)) {
        console.error(`Token ${token} carries invalid payload`);
        return null;
      }
      return payload;
    } catch (e) {
      console.error(`Could not parse token ${token}`);
      return null;
    }
  }
}
