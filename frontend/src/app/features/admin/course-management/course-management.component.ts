import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ReactiveFormsModule, FormBuilder, FormGroup, Validators } from '@angular/forms';
import { CourseService } from '../../../core/services/course.service';
import { TrainerService } from '../../../core/services/trainer.service';
import { Course, Trainer } from '../../../core/models/api.models';

@Component({
  selector: 'app-course-management',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule],
  template: `
    <div class="course-management-container">
      <header class="header">
        <h1>Course Management</h1>
        <p>Manage courses and assign trainers</p>
      </header>

      <div class="content-grid">
        <!-- Form Section -->
        <div class="form-section card">
          <h2>{{ editingId ? 'Edit Course' : 'Add New Course' }}</h2>
          <form [formGroup]="courseForm" (ngSubmit)="onSubmit()" class="course-form">
            <div class="form-group">
              <label>Course Code</label>
              <input type="text" formControlName="code" class="form-control" placeholder="e.g., JAVA101">
            </div>

            <div class="form-group">
              <label>Course Title</label>
              <input type="text" formControlName="title" class="form-control" placeholder="e.g., Java Programming">
            </div>

            <div class="form-group">
              <label>Description</label>
              <textarea formControlName="description" class="form-control" rows="3" placeholder="Course description..."></textarea>
            </div>

            <div class="form-group">
              <label>Assign Trainer</label>
              <select formControlName="trainerId" class="form-control">
                <option value="" [disabled]="true">Select a trainer...</option>
                <option *ngFor="let trainer of trainers" [value]="trainer.id">
                  {{ trainer.name }} ({{ trainer.specialty }})
                </option>
              </select>
            </div>

            <div class="form-actions">
              <button type="submit" [disabled]="courseForm.invalid || isSubmitting" class="btn-primary">
                {{ editingId ? 'Update Course' : 'Create Course' }}
              </button>
              <button *ngIf="editingId" type="button" (click)="cancelEdit()" class="btn-secondary">
                Cancel
              </button>
              <button type="button" (click)="courseForm.reset()" class="btn-ghost">
                Clear Form
              </button>
            </div>

            <div *ngIf="statusMsg" class="alert" [class.success]="statusType === 'success'" [class.error]="statusType === 'error'">
              {{ statusMsg }}
            </div>
          </form>
        </div>

        <!-- Courses List Section -->
        <div class="list-section card">
          <h2>All Courses ({{ courses.length }})</h2>
          <div class="courses-list">
            <div *ngIf="courses.length === 0" class="empty-state">
              No courses found. Create one to get started.
            </div>
            <div *ngFor="let course of courses" class="course-item">
              <div class="course-header">
                <div class="course-code">{{ course.code }}</div>
                <h3>{{ course.title }}</h3>
              </div>
              <p class="description">{{ course.description || 'No description' }}</p>
              <div class="course-meta">
                <span class="trainer" *ngIf="course.trainerName">
                  <strong>Trainer:</strong> {{ course.trainerName }}
                </span>
                <span class="no-trainer" *ngIf="!course.trainerName">
                  <strong>Trainer:</strong> Unassigned
                </span>
              </div>
              <div class="course-actions">
                <button (click)="editCourse(course)" class="btn-edit">Edit</button>
                <button (click)="deleteCourse(course.id!)" class="btn-delete">Delete</button>
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>
  `,
  styles: [`
    .course-management-container {
      padding: 30px;
      background: #f8fafc;
      min-height: 100vh;
    }

    .header {
      margin-bottom: 30px;
    }

    .header h1 {
      color: #1e293b;
      font-size: 2rem;
      font-weight: 800;
      margin: 0;
    }

    .header p {
      color: #64748b;
      margin-top: 5px;
    }

    .content-grid {
      display: grid;
      grid-template-columns: 1fr 1fr;
      gap: 30px;
    }

    @media (max-width: 1024px) {
      .content-grid {
        grid-template-columns: 1fr;
      }
    }

    .card {
      background: white;
      border-radius: 16px;
      padding: 25px;
      box-shadow: 0 4px 6px -1px rgba(0, 0, 0, 0.1);
      border: 1px solid #e2e8f0;
    }

    h2 {
      font-size: 1.25rem;
      color: #334155;
      margin-bottom: 20px;
      margin-top: 0;
    }

    /* Form Styles */
    .course-form {
      display: flex;
      flex-direction: column;
      gap: 15px;
    }

    .form-group {
      display: flex;
      flex-direction: column;
    }

    .form-group label {
      font-size: 0.875rem;
      font-weight: 600;
      color: #475569;
      margin-bottom: 6px;
    }

    .form-control {
      padding: 10px 12px;
      border: 1px solid #e2e8f0;
      border-radius: 8px;
      background: #f8fafc;
      font-size: 0.9375rem;
      transition: all 0.2s;
      font-family: inherit;
    }

    .form-control:focus {
      outline: none;
      border-color: #6366f1;
      background: white;
      box-shadow: 0 0 0 4px rgba(99, 102, 241, 0.1);
    }

    textarea.form-control {
      resize: vertical;
      min-height: 80px;
    }

    .form-actions {
      display: flex;
      gap: 10px;
      margin-top: 10px;
    }

    .btn-primary,
    .btn-secondary,
    .btn-ghost {
      padding: 10px 16px;
      border-radius: 8px;
      border: none;
      font-weight: 600;
      cursor: pointer;
      transition: all 0.2s;
      font-size: 0.875rem;
    }

    .btn-primary {
      background: #6366f1;
      color: white;
      flex: 1;
    }

    .btn-primary:hover:not(:disabled) {
      background: #4f46e5;
    }

    .btn-primary:disabled {
      opacity: 0.5;
      cursor: not-allowed;
    }

    .btn-secondary {
      background: #f1f5f9;
      color: #475569;
      flex: 1;
    }

    .btn-secondary:hover {
      background: #e2e8f0;
    }

    .btn-ghost {
      background: transparent;
      color: #64748b;
      border: 1px solid #e2e8f0;
    }

    .btn-ghost:hover {
      background: #f8fafc;
    }

    /* Alert Styles */
    .alert {
      margin-top: 15px;
      padding: 12px;
      border-radius: 8px;
      font-size: 0.875rem;
      text-align: center;
    }

    .alert.success {
      background: #ecfdf5;
      color: #059669;
    }

    .alert.error {
      background: #fef2f2;
      color: #dc2626;
    }

    /* Courses List Styles */
    .courses-list {
      display: flex;
      flex-direction: column;
      gap: 15px;
    }

    .empty-state {
      text-align: center;
      padding: 40px 20px;
      color: #94a3b8;
      font-style: italic;
    }

    .course-item {
      background: #f8fafc;
      border: 1px solid #e2e8f0;
      border-radius: 10px;
      padding: 16px;
      transition: all 0.2s;
    }

    .course-item:hover {
      border-color: #cbd5e1;
      box-shadow: 0 4px 12px rgba(0, 0, 0, 0.05);
    }

    .course-header {
      display: flex;
      align-items: center;
      gap: 12px;
      margin-bottom: 10px;
    }

    .course-code {
      background: #6366f1;
      color: white;
      padding: 6px 10px;
      border-radius: 6px;
      font-weight: 700;
      font-size: 0.75rem;
      white-space: nowrap;
    }

    .course-item h3 {
      font-size: 1rem;
      color: #1e293b;
      margin: 0;
      flex: 1;
    }

    .description {
      font-size: 0.875rem;
      color: #64748b;
      margin: 8px 0;
      line-height: 1.4;
    }

    .course-meta {
      display: flex;
      gap: 16px;
      margin-bottom: 12px;
      font-size: 0.875rem;
    }

    .course-meta span {
      color: #475569;
    }

    .trainer {
      color: #059669;
    }

    .no-trainer {
      color: #f59e0b;
    }

    .course-actions {
      display: flex;
      gap: 8px;
    }

    .btn-edit,
    .btn-delete {
      padding: 6px 12px;
      border-radius: 6px;
      border: none;
      font-size: 0.8125rem;
      font-weight: 600;
      cursor: pointer;
      transition: all 0.2s;
    }

    .btn-edit {
      background: #dbeafe;
      color: #0369a1;
    }

    .btn-edit:hover {
      background: #bfdbfe;
    }

    .btn-delete {
      background: #fee2e2;
      color: #dc2626;
    }

    .btn-delete:hover {
      background: #fecaca;
    }
  `]
})
export class CourseManagementComponent implements OnInit {
  courseForm: FormGroup;
  courses: Course[] = [];
  trainers: Trainer[] = [];
  isSubmitting = false;
  statusMsg = '';
  statusType: 'success' | 'error' = 'success';
  editingId: number | null = null;

  constructor(
    private fb: FormBuilder,
    private courseService: CourseService,
    private trainerService: TrainerService
  ) {
    this.courseForm = this.fb.group({
      code: ['', [Validators.required, Validators.minLength(2)]],
      title: ['', [Validators.required, Validators.minLength(3)]],
      description: [''],
      trainerId: ['']
    });
  }

  ngOnInit() {
    this.loadCourses();
    this.loadTrainers();
  }

  loadCourses() {
    this.courseService.getAll().subscribe({
      next: (data) => {
        this.courses = data;
      },
      error: (err) => {
        this.statusMsg = 'Failed to load courses';
        this.statusType = 'error';
      }
    });
  }

  loadTrainers() {
    this.trainerService.getAll().subscribe({
      next: (data) => {
        this.trainers = data;
      },
      error: (err) => {
        console.error('Failed to load trainers', err);
      }
    });
  }

  onSubmit() {
    if (this.courseForm.valid) {
      this.isSubmitting = true;
      this.statusMsg = '';

      const courseData: Course = {
        ...this.courseForm.value,
        trainerId: this.courseForm.value.trainerId ? Number(this.courseForm.value.trainerId) : undefined
      };

      if (this.editingId) {
        this.courseService.update(this.editingId, courseData).subscribe({
          next: () => {
            this.isSubmitting = false;
            this.statusMsg = 'Course updated successfully!';
            this.statusType = 'success';
            this.cancelEdit();
            this.loadCourses();
          },
          error: (err) => {
            this.isSubmitting = false;
            this.statusMsg = 'Update failed: ' + (err.error?.message || err.message);
            this.statusType = 'error';
          }
        });
      } else {
        this.courseService.create(courseData).subscribe({
          next: () => {
            this.isSubmitting = false;
            this.statusMsg = 'Course created successfully!';
            this.statusType = 'success';
            this.courseForm.reset();
            this.loadCourses();
          },
          error: (err) => {
            this.isSubmitting = false;
            this.statusMsg = 'Creation failed: ' + (err.error?.message || err.message);
            this.statusType = 'error';
          }
        });
      }
    }
  }

  editCourse(course: Course) {
    this.editingId = course.id || null;
    this.courseForm.patchValue({
      code: course.code,
      title: course.title,
      description: course.description,
      trainerId: course.trainerId
    });
    window.scrollTo({ top: 0, behavior: 'smooth' });
  }

  cancelEdit() {
    this.editingId = null;
    this.courseForm.reset();
    this.statusMsg = '';
  }

  deleteCourse(id: number) {
    if (confirm('Are you sure you want to delete this course?')) {
      this.courseService.delete(id).subscribe({
        next: () => {
          this.statusMsg = 'Course deleted successfully!';
          this.statusType = 'success';
          this.loadCourses();
        },
        error: (err) => {
          this.statusMsg = 'Delete failed: ' + (err.error?.message || err.message);
          this.statusType = 'error';
        }
      });
    }
  }
}
