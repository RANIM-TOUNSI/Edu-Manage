import { Routes } from '@angular/router';
import { authGuard } from './core/guards/auth.guard';
import { adminGuard } from './core/guards/admin.guard';
import { trainerGuard } from './core/guards/trainer.guard';
import { studentGuard } from './core/guards/student.guard';

import { LoginComponent } from './features/auth/login/login.component';
import { MainLayoutComponent } from './shared/layouts/main-layout/main-layout.component';
import { AdminDashboardComponent } from './features/admin/admin-dashboard/admin-dashboard.component';
import { StudentDashboardComponent } from './features/student/student-dashboard/student-dashboard.component';
import { TrainerDashboardComponent } from './features/trainer/trainer-dashboard/trainer-dashboard.component';
import { CourseListComponent } from './features/courses/course-list/course-list.component';
import { EnrollmentManagementComponent } from './features/admin/enrollment/enrollment.component';
import { GradeManagementComponent } from './features/trainer/grade-management/grade-management.component';
import { RegisterComponent } from './features/auth/register/register.component';

export const routes: Routes = [
    {
        path: 'auth',
        children: [
            { path: 'login', component: LoginComponent },
            { path: 'register', component: RegisterComponent },
            { path: '', redirectTo: 'login', pathMatch: 'full' }
        ]
    },
    {
        path: 'admin',
        component: MainLayoutComponent,
        canActivate: [authGuard, adminGuard],
        children: [
            { path: '', component: AdminDashboardComponent },
            { path: 'dashboard', component: AdminDashboardComponent },
            { path: 'courses', component: CourseListComponent },
            { path: 'enrollment', component: EnrollmentManagementComponent }
        ]
    },
    {
        path: 'student',
        component: MainLayoutComponent,
        canActivate: [authGuard, studentGuard],
        children: [
            { path: '', component: StudentDashboardComponent },
            { path: 'dashboard', component: StudentDashboardComponent }
        ]
    },
    {
        path: 'trainer',
        component: MainLayoutComponent,
        canActivate: [authGuard, trainerGuard],
        children: [
            { path: '', component: TrainerDashboardComponent },
            { path: 'dashboard', component: TrainerDashboardComponent },
            { path: 'grades', component: GradeManagementComponent }
        ]
    },
    { path: '', redirectTo: '/auth/login', pathMatch: 'full' },
    { path: '**', redirectTo: '/auth/login' }
];
