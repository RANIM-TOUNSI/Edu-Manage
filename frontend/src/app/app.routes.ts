import { Routes } from '@angular/router';
import { LoginComponent } from './components/login/login.component';
import { StudentDashboardComponent } from './components/dashboard/dashboard.component';
import { authGuard } from './guards/auth.guard';

import { RegisterComponent } from './components/register/register.component';

export const routes: Routes = [
  { path: '', redirectTo: 'login', pathMatch: 'full' },
  { path: 'login', component: LoginComponent },
  { path: 'register', component: RegisterComponent },
  { path: 'dashboard', component: StudentDashboardComponent, canActivate: [authGuard] },
  // Keeping old routes just in case, or removing if we want a clean slate. 
  // User asked for "Student Dashboard". I will prioritize that.
  { path: '**', redirectTo: 'login' }
];
