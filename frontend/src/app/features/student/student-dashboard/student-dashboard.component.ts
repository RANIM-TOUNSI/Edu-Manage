import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { EnrollmentService } from '../../../core/services/enrollment.service';
import { GradeService } from '../../../core/services/grade.service';
import { AuthService } from '../../../core/services/auth.service';
import { PlanningService } from '../../../core/services/planning.service';
import { ReportService } from '../../../core/services/report.service';
import { Enrollment, Grade, User, Seance } from '../../../core/models/api.models';

@Component({
  selector: 'app-student-dashboard',
  standalone: true,
  imports: [CommonModule],
  template: `
    <div class="dashboard-wrapper">
      <div class="glass-container animate-fade-in">
        <header class="dashboard-header">
          <div class="welcome-section">
            <h1 class="gradient-text">Welcome back, {{ currentUser?.username || 'Explorer' }}! 🚀</h1>
            <p class="subtitle">Your academic journey at a glance.</p>
          </div>
          <div class="header-actions">
            <button class="glass-btn primary" (click)="downloadReport()">
              <svg xmlns="http://www.w3.org/2000/svg" width="20" height="20" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"><path d="M21 15v4a2 2 0 0 1-2 2H5a2 2 0 0 1-2-2v-4"/><polyline points="7 10 12 15 17 10"/><line x1="12" y1="15" x2="12" y2="3"/></svg>
              Academic Report
            </button>
          </div>
        </header>

        <div class="stats-row">
          <div class="stat-card blue">
            <div class="stat-info">
              <span class="label">Average Grade</span>
              <span class="value">{{ calculateGpa() | number:'1.2-2' }}</span>
              <div class="progress-container">
                <div class="progress-bar" [style.width.%]="(calculateGpa() / 20) * 100"></div>
              </div>
            </div>
            <div class="stat-icon">📈</div>
          </div>

          <div class="stat-card purple">
            <div class="stat-info">
              <span class="label">Active Courses</span>
              <span class="value">{{ enrollments.length }}</span>
            </div>
            <div class="stat-icon">📚</div>
          </div>

          <div class="stat-card teal">
            <div class="stat-info">
              <span class="label">Upcoming Classes</span>
              <span class="value">{{ schedule.length }}</span>
            </div>
            <div class="stat-icon">📅</div>
          </div>
        </div>

        <div class="main-layout">
          <section class="content-panel">
            <div class="panel-header">
              <h2>Weekly Schedule</h2>
              <span class="badge">{{ schedule.length }} Sessions</span>
            </div>
            
            <div class="timetable-list">
              <div *ngIf="schedule.length === 0" class="empty-state">
                <div class="empty-icon">☕</div>
                <p>No classes scheduled for this week. Time for a break!</p>
              </div>
              
              <div *ngFor="let s of schedule; let i = index" class="sc-card" [style.animation-delay.ms]="i * 100">
                <div class="time-block">
                  <span class="start">{{ s.startTime }}</span>
                  <div class="separator"></div>
                  <span class="end">{{ s.endTime }}</span>
                </div>
                <div class="session-details">
                  <h3>{{ s.courseTitle }}</h3>
                  <div class="tags">
                    <span class="tag room">📍 Room {{ s.room }}</span>
                    <span class="tag date">🗓 {{ s.date | date:'EEE, MMM d' }}</span>
                  </div>
                </div>
                <div class="action-zone">
                  <button class="icon-btn">
                    <svg width="20" height="20" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2"><path d="M12 20h9"/><path d="M16.5 3.5a2.121 2.121 0 0 1 3 3L7 19l-4 1 1-4L16.5 3.5z"/></svg>
                  </button>
                </div>
              </div>
            </div>
          </section>

          <aside class="side-panel">
            <div class="panel-header">
              <h2>Recent Grades</h2>
            </div>
            
            <div class="grades-box">
              <div *ngFor="let g of grades" class="grade-item">
                <div class="course-meta">
                  <span class="title">{{ g.courseTitle || 'Unfinished Course' }}</span>
                  <span class="id">#{{ g.courseId }}</span>
                </div>
                <div class="score-badge" [class.pass]="g.value >= 10" [class.fail]="g.value < 10">
                  {{ g.value }}
                </div>
              </div>
              <div *ngIf="grades.length === 0" class="mini-empty">
                No grades recorded yet.
              </div>
            </div>

            <div class="panel-header" style="margin-top: 2rem;">
              <h2>Enrollments</h2>
            </div>
            <div class="enrollment-stack">
              <div *ngFor="let e of enrollments" class="en-card">
                <div class="dot"></div>
                <div class="en-info">
                  <span class="en-title">Course #{{ e.courseId }}</span>
                  <span class="en-date">since {{ e.enrollmentDate | date }}</span>
                </div>
              </div>
            </div>
          </aside>
        </div>
      </div>
    </div>
  `,
  styles: [`
    :host { --primary: #4f46e5; --bg-gradient: linear-gradient(135deg, #e0e7ff 0%, #f1f5f9 100%); }
    
    .dashboard-wrapper { 
      min-height: 100vh; padding: 2rem; 
      background: var(--bg-gradient);
    }
    
    .glass-container {
      max-width: 1400px; margin: 0 auto;
      background: rgba(255, 255, 255, 0.7);
      backdrop-filter: blur(20px);
      border-radius: 24px;
      padding: 2.5rem;
      border: 1px solid rgba(255, 255, 255, 0.5);
      box-shadow: 0 20px 50px rgba(0,0,0,0.05);
    }

    .dashboard-header { 
      display: flex; justify-content: space-between; align-items: center; 
      margin-bottom: 3rem; 
    }

    .gradient-text {
      background: linear-gradient(90deg, #4f46e5, #9333ea);
      -webkit-background-clip: text;
      -webkit-text-fill-color: transparent;
      font-size: 2.5rem; font-weight: 800; letter-spacing: -0.5px;
    }

    .subtitle { color: #64748b; font-size: 1.1rem; margin-top: 0.5rem; }

    .glass-btn {
      display: flex; align-items: center; gap: 0.75rem;
      padding: 0.875rem 1.5rem; border-radius: 12px;
      font-weight: 600; cursor: pointer; transition: all 0.3s;
      border: none;
    }

    .glass-btn.primary {
      background: #4f46e5; color: white;
      box-shadow: 0 10px 15px -3px rgba(79, 70, 229, 0.3);
    }
    .glass-btn.primary:hover { background: #4338ca; transform: translateY(-2px); }

    /* Stats Row */
    .stats-row { display: grid; grid-template-columns: repeat(3, 1fr); gap: 1.5rem; margin-bottom: 3rem; }
    
    .stat-card {
      padding: 1.5rem; border-radius: 20px; color: white;
      display: flex; justify-content: space-between; align-items: center;
      transition: transform 0.3s; cursor: default;
    }
    .stat-card:hover { transform: translateY(-5px); }
    
    .stat-card.blue { background: linear-gradient(135deg, #3b82f6 0%, #2563eb 100%); }
    .stat-card.purple { background: linear-gradient(135deg, #8b5cf6 0%, #7c3aed 100%); }
    .stat-card.teal { background: linear-gradient(135deg, #14b8a6 0%, #0d9488 100%); }

    .stat-info .label { display: block; font-size: 0.9rem; opacity: 0.9; font-weight: 500; margin-bottom: 0.25rem; }
    .stat-info .value { font-size: 2rem; font-weight: 800; display: block; }
    
    .progress-container { width: 120px; height: 6px; background: rgba(255,255,255,0.2); border-radius: 3px; margin-top: 1rem; }
    .progress-bar { height: 100%; background: white; border-radius: 3px; transition: width 1s ease-out; }

    .stat-icon { font-size: 2.5rem; opacity: 0.3; }

    /* Layout */
    .main-layout { display: grid; grid-template-columns: 1fr 380px; gap: 2.5rem; }
    
    .panel-header { display: flex; justify-content: space-between; align-items: center; margin-bottom: 1.5rem; }
    .panel-header h2 { font-size: 1.4rem; font-weight: 700; color: #1e293b; }
    
    .badge { background: rgba(79, 70, 229, 0.1); color: #4f46e5; padding: 0.25rem 0.75rem; border-radius: 20px; font-size: 0.8rem; font-weight: 600; }

    /* Timetable SC-Card */
    .timetable-list { display: flex; flex-direction: column; gap: 1.25rem; }
    .sc-card {
      background: white; border-radius: 16px; padding: 1.25rem;
      display: flex; align-items: center; gap: 1.5rem;
      border: 1px solid rgba(0,0,0,0.05); transition: 0.3s;
      animation: slideUp 0.5s ease-out forwards; opacity: 0;
    }
    .sc-card:hover { border-color: #4f46e5; box-shadow: 0 10px 20px rgba(0,0,0,0.03); transform: scale(1.01); }

    .time-block { display: flex; flex-direction: column; align-items: center; width: 80px; }
    .time-block .start { font-weight: 800; color: #1e293b; font-size: 1.1rem; }
    .time-block .end { color: #94a3b8; font-size: 0.85rem; font-weight: 500; }
    .time-block .separator { width: 2px; height: 15px; background: #e2e8f0; margin: 4px 0; }

    .session-details { flex: 1; }
    .session-details h3 { font-size: 1.15rem; font-weight: 700; color: #1e293b; margin-bottom: 0.5rem; }
    
    .tags { display: flex; gap: 0.75rem; }
    .tag { padding: 0.25rem 0.6rem; border-radius: 6px; font-size: 0.8rem; font-weight: 500; }
    .tag.room { background: #f1f5f9; color: #475569; }
    .tag.date { background: #eff6ff; color: #3b82f6; }

    .icon-btn { 
      width: 40px; height: 40px; border-radius: 10px; border: none; background: #f8fafc; 
      color: #94a3b8; cursor: pointer; transition: 0.2s;
    }
    .icon-btn:hover { background: #e0e7ff; color: #4f46e5; }

    /* Grades */
    .grades-box { background: white; border-radius: 20px; padding: 1.5rem; border: 1px solid rgba(0,0,0,0.05); }
    .grade-item { display: flex; justify-content: space-between; align-items: center; padding: 1rem 0; border-bottom: 1px dotted #e2e8f0; }
    .grade-item:last-child { border: none; }
    
    .course-meta .title { display: block; font-weight: 600; color: #1e293b; font-size: 0.95rem; }
    .course-meta .id { font-size: 0.8rem; color: #94a3b8; }
    
    .score-badge { 
      font-weight: 800; font-size: 1.1rem; width: 45px; height: 45px;
      display: flex; align-items: center; justify-content: center; border-radius: 12px;
    }
    .score-badge.pass { background: #dcfce7; color: #166534; }
    .score-badge.fail { background: #fee2e2; color: #991b1b; }

    /* Enrollments */
    .enrollment-stack { display: flex; flex-direction: column; gap: 0.75rem; }
    .en-card { 
      display: flex; align-items: center; gap: 1rem; padding: 1rem;
      background: rgba(255,255,255,0.5); border-radius: 16px; border: 1px solid white;
    }
    .en-card .dot { width: 10px; height: 10px; border-radius: 50%; background: #4f46e5; }
    .en-info .en-title { display: block; font-weight: 600; font-size: 0.9rem; }
    .en-info .en-date { font-size: 0.75rem; color: #64748b; }

    .empty-state { text-align: center; padding: 3rem; color: #64748b; }
    .empty-icon { font-size: 3rem; margin-bottom: 1rem; }

    @keyframes slideUp {
      from { opacity: 0; transform: translateY(20px); }
      to { opacity: 1; transform: translateY(0); }
    }
  `]

})
export class StudentDashboardComponent implements OnInit {
  enrollments: Enrollment[] = [];
  grades: Grade[] = [];
  schedule: Seance[] = [];
  currentUser: User | null = null;

  constructor(
    private enrollmentService: EnrollmentService,
    private gradeService: GradeService,
    private authService: AuthService,
    private planningService: PlanningService,
    private reportService: ReportService
  ) { }

  ngOnInit() {
    this.authService.currentUser.subscribe((user: User | null) => {
      this.currentUser = user;
      if (user && user.studentId) {
        this.enrollmentService.getByStudent(user.studentId).subscribe(data => this.enrollments = data);
        this.gradeService.getByStudent(user.studentId).subscribe(data => this.grades = data);
        this.planningService.getMySchedule().subscribe(data => {
          const today = new Date();
          today.setHours(0, 0, 0, 0);
          this.schedule = data.filter(s => new Date(s.date) >= today);
        });
      }
    });
  }

  calculateGpa(): number {
    if (this.grades.length === 0) return 0;
    const sum = this.grades.reduce((acc, g) => acc + g.value, 0);
    return sum / this.grades.length;
  }

  downloadReport() {
    if (this.currentUser && this.currentUser.studentId) {
      this.reportService.downloadGradesPdf(this.currentUser.studentId).subscribe(blob => {
        const url = window.URL.createObjectURL(blob);
        const a = document.createElement('a');
        a.href = url;
        a.download = `Grades_Report_${this.currentUser?.username}.pdf`;
        a.click();
        window.URL.revokeObjectURL(url);
      });
    }
  }
}

