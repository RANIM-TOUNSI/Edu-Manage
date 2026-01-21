import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ReactiveFormsModule, FormBuilder, FormGroup, Validators } from '@angular/forms';
import { GradeService } from '../../../core/services/grade.service';
import { StudentService } from '../../../core/services/student.service';
import { CourseService } from '../../../core/services/course.service';
import { AuthService } from '../../../core/services/auth.service';
import { Student, Course, User } from '../../../core/models/api.models';

@Component({
  selector: 'app-grade-management',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule],
  template: `
    <div class="grade-container">
      <div class="grade-card">
        <header class="card-header">
          <i class="icon-grad">🎯</i>
          <h1>Academic Grading</h1>
          <p>Assign student grades for courses with precision.</p>
        </header>

        <form [formGroup]="gradeForm" (ngSubmit)="onSubmit()" class="grade-form">
          <div class="input-grid">
            <div class="form-group">
              <label>Select Student</label>
              <div class="select-wrapper">
                <select formControlName="studentId" class="form-control">
                  <option value="" disabled selected>Choose a student...</option>
                  <option *ngFor="let s of students" [value]="s.id">{{ s.firstName }} {{ s.lastName }}</option>
                </select>
              </div>
            </div>

            <div class="form-group">
              <label>Target Course</label>
              <div class="select-wrapper">
                <select formControlName="courseId" class="form-control">
                  <option value="" disabled selected>Choose a course...</option>
                  <option *ngFor="let c of courses" [value]="c.id">{{ c.title }}</option>
                </select>
              </div>
            </div>
          </div>

          <div class="form-group grade-input-group">
            <label>Numerical Grade (0 - 20)</label>
            <div class="grade-value-wrapper">
              <input type="number" formControlName="value" class="form-control value-input" min="0" max="20" placeholder="00">
              <span class="max-badge">/ 20</span>
            </div>
          </div>

          <div class="form-actions">
            <button type="submit" [disabled]="gradeForm.invalid || isSubmitting" class="btn-primary">
              <span *ngIf="!isSubmitting">Verify & Submit</span>
              <span *ngIf="isSubmitting">Processing...</span>
            </button>
            <button type="button" (click)="gradeForm.reset()" class="btn-ghost">Reset Form</button>
          </div>

          <div *ngIf="statusMsg" class="alert" [class.success]="statusType === 'success'" [class.error]="statusType === 'error'">
            {{ statusMsg }}
          </div>
        </form>
      </div>
    </div>
  `,
  styles: [`
    .grade-container { 
      display: flex; justify-content: center; align-items: flex-start; 
      padding: 2rem; background: #f8fafc; min-height: calc(100vh - 72px); 
    }
    .grade-card {
      background: white; width: 100%; max-width: 600px; padding: 3rem;
      border-radius: 24px; box-shadow: 0 10px 25px rgba(0,0,0,0.05);
      border: 1px solid #e2e8f0;
    }
    .card-header { text-align: center; margin-bottom: 2.5rem; }
    .icon-grad { font-size: 2.5rem; display: block; margin-bottom: 1rem; }
    h1 { font-size: 1.75rem; color: #1e293b; margin: 0; font-weight: 800; }
    .card-header p { color: #64748b; margin-top: 0.5rem; }

    .input-grid { display: grid; grid-template-columns: 1fr 1fr; gap: 1.5rem; margin-bottom: 2rem; }
    .form-group label { display: block; font-size: 0.875rem; font-weight: 600; color: #475569; margin-bottom: 0.5rem; }
    
    .form-control {
      width: 100%; padding: 0.75rem 1rem; border-radius: 10px;
      border: 1px solid #e2e8f0; background: #f8fafc; font-size: 0.9375rem;
      transition: all 0.2s;
    }
    .form-control:focus { outline: none; border-color: #6366f1; background: white; box-shadow: 0 0 0 4px rgba(99, 102, 241, 0.1); }

    .grade-value-wrapper { position: relative; display: flex; align-items: center; }
    .value-input { font-size: 2rem; font-weight: 800; text-align: center; padding: 1rem; }
    .max-badge { position: absolute; right: 1.5rem; font-weight: 700; color: #94a3b8; font-size: 1.25rem; }

    .form-actions { display: flex; flex-direction: column; gap: 1rem; margin-top: 2rem; }
    .btn-primary { 
      background: #6366f1; color: white; border: none; padding: 1rem; border-radius: 12px;
      font-weight: 700; cursor: pointer; transition: all 0.3s; font-size: 1rem;
    }
    .btn-primary:hover:not(:disabled) { background: #4f46e5; transform: translateY(-2px); box-shadow: 0 4px 12px rgba(99, 102, 241, 0.2); }
    .btn-primary:disabled { opacity: 0.6; cursor: not-allowed; }

    .btn-ghost { background: transparent; border: none; color: #64748b; font-weight: 600; cursor: pointer; }
    .btn-ghost:hover { color: #1e293b; }

    .alert { margin-top: 1.5rem; padding: 1rem; border-radius: 10px; text-align: center; font-weight: 600; font-size: 0.875rem; }
    .alert.success { background: #ecfdf5; color: #059669; }
    .alert.error { background: #fef2f2; color: #dc2626; }
  `]
})
export class GradeManagementComponent implements OnInit {
  gradeForm: FormGroup;
  students: Student[] = [];
  courses: Course[] = [];
  isSubmitting = false;
  statusMsg = '';
  statusType: 'success' | 'error' = 'success';

  constructor(
    private fb: FormBuilder,
    private gradeService: GradeService,
    private studentService: StudentService,
    private courseService: CourseService,
    private authService: AuthService
  ) {
    this.gradeForm = this.fb.group({
      studentId: ['', Validators.required],
      courseId: ['', Validators.required],
      value: ['', [Validators.required, Validators.min(0), Validators.max(20)]]
    });
  }

  ngOnInit() {
    this.studentService.getAll().subscribe(data => this.students = data);
    
    // Load only trainer's assigned courses
    this.authService.currentUser.subscribe((user: User | null) => {
      if (user && user.trainerId) {
        this.courseService.getByTrainer(user.trainerId).subscribe(data => this.courses = data);
      }
    });
  }

  onSubmit() {
    if (this.gradeForm.valid) {
      this.isSubmitting = true;
      this.statusMsg = '';
      const { studentId, courseId, value } = this.gradeForm.value;

      this.gradeService.assign(studentId, courseId, value).subscribe({
        next: () => {
          this.isSubmitting = false;
          this.statusMsg = 'Grade successfully recorded!';
          this.statusType = 'success';
          this.gradeForm.reset();
        },
        error: (err) => {
          this.isSubmitting = false;
          this.statusMsg = 'Submission failed: ' + err.error?.message || err.message;
          this.statusType = 'error';
        }
      });
    }
  }
}

