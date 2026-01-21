import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ReactiveFormsModule, FormBuilder, FormGroup, Validators } from '@angular/forms';
import { Router, RouterModule } from '@angular/router';
import { AuthService } from '../../../core/services/auth.service';

@Component({
    selector: 'app-login',
    standalone: true,
    imports: [CommonModule, ReactiveFormsModule, RouterModule],
    templateUrl: './login.component.html',
    styleUrls: ['./login.component.scss']
})
export class LoginComponent {
    loginForm: FormGroup;
    error: string = '';
    loading: boolean = false;

    constructor(
        private fb: FormBuilder,
        private authService: AuthService,
        private router: Router
    ) {
        this.loginForm = this.fb.group({
            username: ['', [Validators.required, Validators.minLength(3)]],
            password: ['', [Validators.required, Validators.minLength(4)]]
        });
    }

    onSubmit() {
        if (this.loginForm.valid) {
            this.loading = true;
            this.error = '';

            this.authService.login(this.loginForm.value).subscribe({
                next: (res) => {
                    this.loading = false;
                    const role = res.user.role;
                    this.router.navigate([`/${role.toLowerCase()}`]);
                },
                error: (err) => {
                    this.loading = false;
                    this.error = 'Access denied. Please check your credentials.';
                }
            });
        }
    }
}
