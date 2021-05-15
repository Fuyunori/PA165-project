import { Component } from '@angular/core';
import { Router } from '@angular/router';
import { AuthService } from 'src/app/services/auth.service';

@Component({
  selector: 'tc-main',
  templateUrl: './main.component.html',
  styleUrls: ['./main.component.scss'],
})
export class MainComponent {
  constructor(
    private readonly auth: AuthService,
    private readonly router: Router,
  ) {}

  logOut(): void {
    this.auth.logOut();
    this.router.navigateByUrl('/landing');
  }
}
