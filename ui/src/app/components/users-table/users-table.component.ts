import { Component, OnDestroy, OnInit } from '@angular/core';
import { UserService } from '../../services/user.service';
import { AuthService } from '../../services/auth.service';

@Component({
  selector: 'tc-users-table',
  templateUrl: './users-table.component.html',
  styleUrls: ['./users-table.component.scss'],
})
export class UsersTableComponent implements OnInit {
  readonly users$ = this.userService.orderedUsers$;
  readonly displayedColumns: string[] = ['id', 'username', 'name', 'role'];

  constructor(
    private readonly auth: AuthService,
    private readonly userService: UserService,
  ) {}

  ngOnInit(): void {
    this.userService.getUsers();
  }
}
