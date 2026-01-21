import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { CourseService } from '../../../core/services/course.service';
import { AuthService } from '../../../core/services/auth.service';
import { PlanningService } from '../../../core/services/planning.service';
import { Course, User, Seance } from '../../../core/models/api.models';
import { RouterModule } from '@angular/router';

@Component({
  selector: 'app-trainer-dashboard',
  standalone: true,
  imports: [CommonModule, RouterModule],
  template: `
    <div class="dashboard-container">
      <header class="dashboard-header">
        <div class="welcome-text">
          <h1>Trainer Control Center</h1>
          <p>Overview of your assigned courses and teaching schedule.</p>
        </div>
      </header>

      <div class="dashboard-grid">
        <section class="main-content">
          <div class="card">
            <h2><i class="icon">📚</i> My Assigned Courses</h2>
            <div class="course-grid">
              <div *ngFor="let c of courses" class="course-card">
                <div class="course-badge">{{ c.code }}</div>
                <h3>{{ c.title }}</h3>
                <p>{{ c.description || 'No description provided.' }}</p>
                <div class="card-footer">
                  <a [routerLink]="['/trainer/grades']" class="btn-secondary">Manage Grades</a>
                </div>
              </div>
            </div>
            <div *ngIf="courses.length === 0" class="empty-state">
              You are not assigned to any courses yet.
            </div>
          </div>
        </section>

        <aside class="sidebar-content">
          <section class="card timetable-card">
            <h2><i class="icon">📅</i> Upcoming Sessions</h2>
            <div class="timetable-list">
              <div *ngFor="let s of schedule" class="timetable-item">
                <div class="time">{{ s.startTime }}</div>
                <div class="details">
                  <span class="course-title">{{ s.courseTitle }}</span>
                  <span class="group-info">Group: {{ s.groupName }} | Room: {{ s.room }}</span>
                </div>
                <div class="date">{{ s.date | date:'MMM d' }}</div>
              </div>
              <div *ngIf="schedule.length === 0" class="empty-state">
                No upcoming sessions scheduled.
              </div>
            </div>
          </section>
        </aside>
      </div>
    </div>
  `,
  styles: [`
    .dashboard-container { padding: 30px; background: #f8fafc; min-height: 100vh; }
    .dashboard-header { margin-bottom: 30px; }
    h1 { color: #1e293b; margin: 0; font-size: 2.25rem; font-weight: 800; letter-spacing: -0.5px; }
    p { color: #64748b; margin-top: 5px; }
    
    .dashboard-grid { 
      display: grid; grid-template-columns: 1fr 400px; gap: 30px; 
    }
    
    .card { 
      background: white; border-radius: 16px; padding: 25px; 
      box-shadow: 0 4px 6px -1px rgba(0, 0, 0, 0.1); 
      border: 1px solid #e2e8f0;
    }
    h2 { font-size: 1.25rem; color: #334155; margin-bottom: 25px; display: flex; align-items: center; gap: 10px; }

    .course-grid { 
      display: grid; grid-template-columns: repeat(auto-fill, minmax(300px, 1fr)); gap: 20px; 
    }
    .course-card { 
      background: #f8fafc; border: 1px solid #e2e8f0; border-radius: 12px; padding: 20px;
      transition: all 0.2s;
    }
    .course-card:hover { transform: translateY(-4px); box-shadow: 0 10px 15px -3px rgba(0, 0, 0, 0.05); border-color: #cbd5e1; }
    .course-badge { background: #6366f1; color: white; display: inline-block; padding: 4px 10px; border-radius: 6px; font-size: 0.75rem; font-weight: 700; margin-bottom: 12px; }
    .course-card h3 { margin: 0 0 10px; font-size: 1.125rem; color: #1e293b; }
    .course-card p { font-size: 0.875rem; color: #64748b; line-height: 1.5; height: 3em; overflow: hidden; }
    .card-footer { margin-top: 20px; padding-top: 15px; border-top: 1px solid #e2e8f0; }

    .btn-secondary { 
      color: #6366f1; text-decoration: none; font-weight: 600; font-size: 0.875rem;
      display: flex; align-items: center; gap: 5px; transition: color 0.2s;
    }
    .btn-secondary:hover { color: #4338ca; }

    /* Timetable Styling */
    .timetable-list { display: flex; flex-direction: column; gap: 15px; }
    .timetable-item { 
      display: grid; grid-template-columns: 60px 1fr 60px; 
      align-items: center; padding: 15px; background: #fff; 
      border: 1px solid #e2e8f0; border-radius: 10px;
    }
    .time { font-weight: 700; color: #6366f1; font-size: 0.9rem; }
    .course-title { font-weight: 600; color: #1e293b; display: block; font-size: 0.95rem; }
    .group-info { font-size: 0.75rem; color: #94a3b8; }
    .date { text-align: right; font-weight: 600; color: #64748b; font-size: 0.85rem; }

    .empty-state { text-align: center; padding: 40px; color: #94a3b8; font-style: italic; }
  `]
})
export class TrainerDashboardComponent implements OnInit {
  courses: Course[] = [];
  schedule: Seance[] = [];

  constructor(
    private courseService: CourseService,
    private authService: AuthService,
    private planningService: PlanningService
  ) { }

  ngOnInit() {
    this.authService.currentUser.subscribe((user: User | null) => {
      if (user && user.trainerId) {
        this.courseService.getByTrainer(user.trainerId).subscribe(data => this.courses = data);
        this.planningService.getTrainerSchedule(user.trainerId).subscribe(data => {
          const today = new Date();
          today.setHours(0, 0, 0, 0);
          this.schedule = data.filter(s => new Date(s.date) >= today);
        });
      }
    });
  }
}

