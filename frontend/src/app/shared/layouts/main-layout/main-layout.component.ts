import { Component, Inject, PLATFORM_ID, OnInit } from '@angular/core';
import { isPlatformBrowser, CommonModule } from '@angular/common';
import { RouterModule, Router } from '@angular/router';
import { AuthService } from '../../../core/services/auth.service';
import { User } from '../../../core/models/api.models';

@Component({
  selector: 'app-main-layout',
  standalone: true,
  imports: [CommonModule, RouterModule],
  template: `
    <div class="layout-container">
      <aside class="sidebar">
        <div class="sidebar-header">
          <span class="icon">🎓</span>
          <span class="title">EduManage</span>
        </div>
        
        <nav class="sidebar-nav">
          <!-- Common for all roles -->
          <ng-container *ngIf="isRole('STUDENT')">
            <a routerLink="/student/dashboard" routerLinkActive="active" class="nav-item">
              <i class="icon">🏠</i> Dashboard
            </a>
          </ng-container>

          <ng-container *ngIf="isRole('TRAINER')">
            <a routerLink="/trainer/dashboard" routerLinkActive="active" class="nav-item">
              <i class="icon">🏠</i> Dashboard
            </a>
            <a routerLink="/trainer/grades" routerLinkActive="active" class="nav-item">
              <i class="icon">📝</i> Grade Management
            </a>
          </ng-container>

          <ng-container *ngIf="isRole('ADMIN')">
            <a routerLink="/admin/dashboard" routerLinkActive="active" class="nav-item">
              <i class="icon">📊</i> Overview
            </a>
            <a routerLink="/admin/courses" routerLinkActive="active" class="nav-item">
              <i class="icon">📚</i> Courses
            </a>
            <a routerLink="/admin/enrollment" routerLinkActive="active" class="nav-item">
              <i class="icon">👥</i> Enrollments
            </a>
          </ng-container>
        </nav>

        <div class="sidebar-footer">
          <button (click)="logout()" class="btn-logout">
            <i class="icon">🚪</i> Sign Out
          </button>
        </div>
      </aside>

      <main class="content-area">
        <header class="top-bar">
          <div class="top-bar-left">
            <span class="breadcrumb">Portal / {{ userRole }}</span>
          </div>
          <div class="top-bar-right">
            <div class="user-profile">
              <div class="user-meta">
                <span class="user-name">{{ userName }}</span>
                <span class="role-badge">{{ userRole }}</span>
              </div>
              <div class="user-avatar">{{ userName.charAt(0) }}</div>
            </div>
          </div>
        </header>
        <div class="page-content">
          <router-outlet></router-outlet>
        </div>
      </main>
    </div>
  `,
  styles: [`
    .layout-container { display: flex; height: 100vh; overflow: hidden; font-family: 'Inter', sans-serif; }
    
    .sidebar {
      width: 280px;
      background: #0f172a;
      color: white;
      display: flex;
      flex-direction: column;
      padding: 2rem 1.25rem;
      box-shadow: 4px 0 10px rgba(0,0,0,0.1);
    }

    .sidebar-header {
      display: flex;
      align-items: center;
      gap: 1rem;
      font-size: 1.5rem;
      font-weight: 800;
      margin-bottom: 3rem;
      color: #6366f1;
      .icon { font-size: 1.8rem; }
      .title { color: white; letter-spacing: -0.5px; }
    }

    .sidebar-nav {
      flex: 1;
      display: flex;
      flex-direction: column;
      gap: 0.75rem;
    }

    .nav-item {
      padding: 0.875rem 1.25rem;
      border-radius: 12px;
      color: #94a3b8;
      text-decoration: none;
      transition: all 0.3s cubic-bezier(0.4, 0, 0.2, 1);
      display: flex;
      align-items: center;
      gap: 0.75rem;
      font-weight: 500;
      
      &:hover {
        background: rgba(255,255,255,0.08);
        color: white;
        transform: translateX(4px);
      }
      
      &.active {
        background: linear-gradient(135deg, #6366f1 0%, #4f46e5 100%);
        color: white;
        box-shadow: 0 4px 12px rgba(99, 102, 241, 0.3);
      }

      .icon { font-style: normal; width: 20px; }
    }

    .sidebar-footer {
      margin-top: auto;
      padding-top: 1.5rem;
      border-top: 1px solid rgba(255,255,255,0.1);
    }

    .btn-logout {
      width: 100%;
      background: rgba(239, 68, 68, 0.1);
      border: 1px solid rgba(239, 68, 68, 0.2);
      color: #f87171;
      padding: 0.875rem;
      border-radius: 12px;
      cursor: pointer;
      display: flex;
      align-items: center;
      justify-content: center;
      gap: 0.5rem;
      font-weight: 600;
      transition: all 0.2s;
      &:hover { background: #ef4444; color: white; border-color: #ef4444; }
    }

    .content-area {
      flex: 1;
      display: flex;
      flex-direction: column;
      background: #f8fafc;
      overflow-y: auto;
    }

    .top-bar {
      height: 72px;
      background: rgba(255, 255, 255, 0.8);
      backdrop-filter: blur(8px);
      border-bottom: 1px solid #e2e8f0;
      display: flex;
      align-items: center;
      justify-content: space-between;
      padding: 0 2.5rem;
      position: sticky;
      top: 0;
      z-index: 10;
    }

    .breadcrumb { color: #64748b; font-size: 0.875rem; font-weight: 500; }

    .user-profile {
      display: flex;
      align-items: center;
      gap: 1rem;
    }

    .user-meta {
      display: flex;
      flex-direction: column;
      align-items: flex-end;
    }

    .user-name { font-weight: 600; color: #1e293b; font-size: 0.9375rem; }

    .role-badge {
      font-size: 0.7rem;
      font-weight: 700;
      color: #6366f1;
      text-transform: uppercase;
      letter-spacing: 0.05em;
    }

    .user-avatar {
      width: 42px;
      height: 42px;
      background: #6366f1;
      color: white;
      border-radius: 12px;
      display: flex;
      align-items: center;
      justify-content: center;
      font-weight: 700;
      font-size: 1.125rem;
      box-shadow: 0 4px 6px -1px rgba(0, 0, 0, 0.1);
    }

    .page-content { padding: 2.5rem; max-width: 1400px; width: 100%; margin: 0 auto; }
  `]
})
export class MainLayoutComponent implements OnInit {
  userRole: string = 'STUDENT';
  userName: string = 'User';

  constructor(
    private authService: AuthService,
    private router: Router,
    @Inject(PLATFORM_ID) private platformId: Object
  ) { }

  ngOnInit() {
    this.authService.currentUser.subscribe(user => {
      if (user) {
        this.userRole = user.role;
        this.userName = user.username;
      } else if (isPlatformBrowser(this.platformId)) {
        this.userRole = localStorage.getItem('userRole') || 'STUDENT';
      }
    });
  }

  isRole(role: string): boolean {
    return this.userRole === role;
  }

  logout() {
    this.authService.logout();
    this.router.navigate(['/auth/login']);
  }
}

