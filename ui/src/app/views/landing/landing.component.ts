import { Component, OnDestroy, OnInit } from '@angular/core';
import { FormBuilder } from '@angular/forms';
import { Router } from '@angular/router';
import { Subject } from 'rxjs';
import { filter, take, takeUntil } from 'rxjs/operators';
import { AuthService } from '../../services/auth.service';

enum LoginFormKey {
  Username = 'Username',
  Password = 'Password',
}

@Component({
  selector: 'tc-landing',
  templateUrl: './landing.component.html',
  styleUrls: ['./landing.component.scss'],
})
export class LandingComponent implements OnInit, OnDestroy {
  readonly loginFailed$ = this.auth.loginFailed$;

  readonly LoginFormKey = LoginFormKey;
  readonly loginForm = this.fb.group({
    [LoginFormKey.Username]: '',
    [LoginFormKey.Password]: '',
  });

  private readonly unsubscribe$ = new Subject<void>();

  constructor(
    private readonly fb: FormBuilder,
    private readonly auth: AuthService,
    private readonly router: Router,
  ) {}

  ngOnInit(): void {
    this.auth.loginFailed$
      .pipe(takeUntil(this.unsubscribe$))
      .subscribe(loginFailed => {
        Object.values(this.loginForm.controls).forEach(control => {
          control.setErrors(loginFailed ? { loginFailed } : null);
        });
      });

    this.auth.loggedIn$
      .pipe(
        filter(loggedIn => loggedIn),
        take(1),
      )
      .subscribe(() => {
        this.router.navigateByUrl('/main');
      });
  }

  ngOnDestroy(): void {
    this.unsubscribe$.next();
    this.unsubscribe$.complete();
  }

  logIn(): void {
    const { value } = this.loginForm;
    this.auth.logIn({
      username: value[LoginFormKey.Username],
      password: value[LoginFormKey.Password],
    });
  }
}
