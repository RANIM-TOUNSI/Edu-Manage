import { Routes } from '@angular/router';
import { AdminDashboardComponent } from './admin-dashboard/admin-dashboard.component';
import { CourseManagementComponent } from './course-management/course-management.component';

export const ADMIN_ROUTES: Routes = [
    { path: 'dashboard', component: AdminDashboardComponent },
    { path: 'courses', component: CourseManagementComponent },
    { path: '', redirectTo: 'dashboard', pathMatch: 'full' }
];
