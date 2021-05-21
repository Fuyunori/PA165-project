import { Component, EventEmitter, Input, Output } from '@angular/core';
import { FormBuilder, Validators } from '@angular/forms';
import { UserRole, UnknownUser } from '../../models/user.model';

enum UserFormKey {
    Name = 'Name',
    Email = 'Email',
    Username = 'Username',
    Role = 'Role',
}

@Component({
    selector: 'tc-user-form',
    templateUrl: './user-form.component.html',
    styleUrls: ['./user-form.component.scss'],
})
export class UserFormComponent {
    @Output() readonly cancelClick = new EventEmitter<void>();
    @Output() readonly userChange = new EventEmitter<UnknownUser>();

    @Input()
    set user(user: UnknownUser) {
        this.userForm.setValue({
            [UserFormKey.Name]: user.name,
            [UserFormKey.Email]: user.email,
            [UserFormKey.Username]: user.username,
            [UserFormKey.Role]: user.role,
        });
    }

    @Input() readOnly = false;
    @Input() submitButtonText = 'Submit';

    readonly UserFormKey = UserFormKey;
    readonly UserRole = UserRole;

    readonly userForm = this.fb.group({
        [UserFormKey.Name]: ['', Validators.required],
        [UserFormKey.Email]: ['', Validators.required],
        [UserFormKey.Username]: ['', Validators.required],
        [UserFormKey.Role]: ['', Validators.required],
    });

    constructor(private readonly fb: FormBuilder) {}

    submit(): void {
        const { value } = this.userForm;

        const user: UnknownUser = {
            name: value[UserFormKey.Name],
            email: value[UserFormKey.Email],
            username: value[UserFormKey.Username],
            role: value[UserFormKey.Role],
        };

        this.userForm.markAsPristine();
        this.userChange.emit(user);
    }

    cancel(): void {
        this.cancelClick.emit();
    }
}
