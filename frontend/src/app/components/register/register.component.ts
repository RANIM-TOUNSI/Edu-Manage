import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ReactiveFormsModule, FormBuilder, FormGroup, Validators } from '@angular/forms';
import { AuthService } from '../../services/auth.service';
import { Router, RouterLink } from '@angular/router';

@Component({
    selector: 'app-register',
    standalone: true,
    imports: [CommonModule, ReactiveFormsModule, RouterLink],
    templateUrl: './register.component.html'
})
export class RegisterComponent {
    registerForm: FormGroup;
    successMessage: string = '';
    errorMessage: string = '';

    constructor(
        private fb: FormBuilder,
        private authService: AuthService,
        private router: Router
    ) {
        this.registerForm = this.fb.group({
            nom: ['', Validators.required],
            prenom: ['', Validators.required],
            email: ['', [Validators.required, Validators.email]],
            password: ['', [Validators.required, Validators.minLength(6)]],
            confirmPassword: ['', Validators.required]
        }, { validators: this.passwordMatchValidator });
    }

    passwordMatchValidator(g: FormGroup) {
        const password = g.get('password')?.value;
        const confirmPassword = g.get('confirmPassword')?.value;
        return password === confirmPassword ? null : { mismatch: true };
    }

    onSubmit() {
        if (this.registerForm.valid) {
            const { confirmPassword, ...request } = this.registerForm.value;
            this.authService.register(request).subscribe({
                next: (response) => {
                    if (response.success) {
                        this.successMessage = 'Inscription réussie! Redirection vers la connexion...';
                        setTimeout(() => this.router.navigate(['/login']), 2000);
                    } else {
                        this.errorMessage = response.message;
                    }
                },
                error: (err) => {
                    this.errorMessage = err.error?.message || 'Erreur lors de l\'inscription.';
                }
            });
        }
    }
}
