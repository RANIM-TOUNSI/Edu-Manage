import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ReactiveFormsModule, FormBuilder, FormGroup, Validators } from '@angular/forms';
import { EnrollmentService } from '../../../core/services/enrollment.service';
import { StudentService } from '../../../core/services/student.service';
import { CourseService } from '../../../core/services/course.service';
import { Student, Course } from '../../../core/models/api.models';

@Component({
  selector: 'app-enrollment-management',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule],
  template: `
    <div class="container">
      <h2>Manual Enrollment</h2>
      <form [formGroup]="enrollForm" (ngSubmit)="onSubmit()" class="enroll-form">
        <div class="form-group">
          <label>Student</label>
          <select formControlName="studentId" class="form-control">
            <option *ngFor="let s of students" [value]="s.id">{{ s.firstName }} {{ s.lastName }}</option>
          </select>
        </div>
        <div class="form-group">
          <label>Course</label>
          <select formControlName="courseId" class="form-control">
            <option *ngFor="let c of courses" [value]="c.id">{{ c.title }}</option>
          </select>
        </div>
        <button type="submit" [disabled]="enrollForm.invalid" class="btn btn-success">Enroll Student</button>
      </form>
    </div>
  `,
  styles: [`
    .enroll-form { max-width: 400px; margin-top: 20px; }
    .form-group { margin-bottom: 15px; }
    .btn-success { background: #4caf50; color: white; padding: 10px 20px; border: none; cursor: pointer; border-radius: 4px; }
  `]
})
export class EnrollmentManagementComponent implements OnInit {
  enrollForm: FormGroup;
  students: Student[] = [];
  courses: Course[] = [];

  constructor(
    private fb: FormBuilder,
    private enrollmentService: EnrollmentService,
    private studentService: StudentService,
    private courseService: CourseService
  ) {
    this.enrollForm = this.fb.group({
      studentId: ['', Validators.required],
      courseId: ['', Validators.required]
    });
  }

  ngOnInit() {
    this.studentService.getAll().subscribe((data: Student[]) => this.students = data);
    this.courseService.getAll().subscribe((data: Course[]) => this.courses = data);
  }

  onSubmit() {
    if (this.enrollForm.valid) {
      const { studentId, courseId } = this.enrollForm.value;
      this.enrollmentService.enroll(studentId, courseId).subscribe({
        next: () => {
          alert('Enrolled successfully!');
          this.enrollForm.reset();
        },
        error: (err) => alert('Enrollment failed: ' + err.message)
      });
    }
  }
}
