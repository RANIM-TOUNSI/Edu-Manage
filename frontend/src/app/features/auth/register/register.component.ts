import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ReactiveFormsModule, FormBuilder, FormGroup, Validators } from '@angular/forms';
import { Router, RouterModule } from '@angular/router';
import { AuthService } from '../../../core/services/auth.service';

@Component({
  selector: 'app-register',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule, RouterModule],
  template: `
    <div class="auth-wrapper animate-fade-in">
      <div class="auth-card">
        <div class="auth-header">
          <h1>Create Account</h1>
          <p>Join our learning community today</p>
        </div>

        <form [formGroup]="registerForm" (ngSubmit)="onSubmit()">
          <div *ngIf="error" class="error-message">
            {{ error }}
          </div>

          <div class="form-grid">
            <div class="form-group">
              <label for="firstName">First Name</label>
              <input id="firstName" type="text" formControlName="firstName" placeholder="John">
            </div>
            <div class="form-group">
              <label for="lastName">Last Name</label>
              <input id="lastName" type="text" formControlName="lastName" placeholder="Doe">
            </div>
          </div>

          <div class="form-group">
            <label for="email">Email Address</label>
            <input id="email" type="email" formControlName="email" placeholder="john@example.com">
          </div>

          <div class="form-group">
            <label for="username">Username</label>
            <input id="username" type="text" formControlName="username" placeholder="johndoe">
          </div>

          <div class="form-group">
            <label for="password">Password</label>
            <input id="password" type="password" formControlName="password" placeholder="••••••••">
          </div>

          <div class="form-group">
            <label for="role">I am a...</label>
            <select id="role" formControlName="role">
              <option value="STUDENT">Student</option>
              <option value="TRAINER">Trainer</option>
            </select>
          </div>

          <button type="submit" [disabled]="registerForm.invalid || loading" class="btn-primary">
            <span *ngIf="!loading">Create Account</span>
            <span *ngIf="loading">Creating...</span>
          </button>

          <div class="auth-footer">
            <p>Already have an account? <a routerLink="/auth/login">Sign In</a></p>
          </div>
        </form>
      </div>
    </div>
  `,
  styles: [``]
})
export class RegisterComponent {
  registerForm: FormGroup;
  error: string = '';
  loading: boolean = false;

  constructor(
    private fb: FormBuilder,
    private authService: AuthService,
    private router: Router
  ) {
    this.registerForm = this.fb.group({
      username: ['', [Validators.required, Validators.minLength(3)]],
      password: ['', [Validators.required, Validators.minLength(4)]],
      firstName: ['', [Validators.required]],
      lastName: ['', [Validators.required]],
      email: ['', [Validators.required, Validators.email]],
      role: ['STUDENT', [Validators.required]]
    });
  }

  onSubmit() {
    if (this.registerForm.valid) {
      this.loading = true;
      this.error = '';

      this.authService.register(this.registerForm.value).subscribe({
        next: () => {
          this.loading = false;
          alert('Registration successful! Please login.');
          this.router.navigate(['/auth/login']);
        },
        error: (err) => {
          this.loading = false;
          this.error = err.error?.message || err.error || 'Registration failed. Please try again.';
        }
      });
    }
  }
}
