import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { CourseService } from '../../../core/services/course.service';
import { AuthService } from '../../../core/services/auth.service';
import { Course, User } from '../../../core/models/api.models';

@Component({
  selector: 'app-course-list',
  standalone: true,
  imports: [CommonModule],
  template: `
    <div class="container">
      <h2>Courses</h2>
      <div class="table-responsive">
        <table class="table">
          <thead>
            <tr>
              <th>Code</th>
              <th>Title</th>
              <th>Trainer</th>
              <th *ngIf="isAdmin">Actions</th>
            </tr>
          </thead>
          <tbody>
            <tr *ngFor="let c of courses">
              <td>{{ c.code }}</td>
              <td>{{ c.title }}</td>
              <td>{{ c.trainerName || 'N/A' }}</td>
              <td *ngIf="isAdmin">
                <button class="btn btn-sm btn-danger" (click)="deleteCourse(c.id!)">Delete</button>
              </td>
            </tr>
          </tbody>
        </table>
      </div>
    </div>
  `,
  styles: [`
    .table { width: 100%; border-collapse: collapse; margin-top: 10px; }
    th, td { padding: 12px; border-bottom: 1px solid #eee; text-align: left; }
    .btn-danger { background: #f44336; color: white; border: none; padding: 5px 10px; cursor: pointer; border-radius: 4px; }
  `]
})
export class CourseListComponent implements OnInit {
  courses: Course[] = [];
  isAdmin: boolean = false;

  constructor(private courseService: CourseService, private authService: AuthService) { }

  ngOnInit() {
    this.courseService.getAll().subscribe((data: Course[]) => this.courses = data);
    this.authService.currentUser.subscribe((user: User | null) => {
      this.isAdmin = user?.role === 'ADMIN';
    });
  }

  deleteCourse(id: number) {
    if (confirm('Are you sure?')) {
      this.courseService.delete(id).subscribe(() => {
        this.courses = this.courses.filter(c => c.id !== id);
      });
    }
  }
}
