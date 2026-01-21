import { Routes } from '@angular/router';
import { TrainerDashboardComponent } from './trainer-dashboard/trainer-dashboard.component';
import { GradeManagementComponent } from './grade-management/grade-management.component';

export const TRAINER_ROUTES: Routes = [
    { path: 'dashboard', component: TrainerDashboardComponent },
    { path: 'grades', component: GradeManagementComponent },
    { path: '', redirectTo: 'dashboard', pathMatch: 'full' }
];
