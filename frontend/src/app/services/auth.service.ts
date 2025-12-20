import { Injectable, Inject, PLATFORM_ID } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Router } from '@angular/router';
import { isPlatformBrowser } from '@angular/common';

@Injectable({
    providedIn: 'root'
})
export class AuthService {
    private apiUrl = 'http://localhost:8081/api/auth';
    private isBrowser: boolean;

    constructor(
        private http: HttpClient,
        private router: Router,
        @Inject(PLATFORM_ID) private platformId: Object
    ) {
        this.isBrowser = isPlatformBrowser(this.platformId);
    }

    login(credentials: any): Observable<any> {
        return this.http.post(`${this.apiUrl}/login`, credentials, {
            withCredentials: true
        });
    }

    register(data: any): Observable<any> {
        return this.http.post(`${this.apiUrl}/register`, data, {
            withCredentials: true
        });
    }

    checkEmail(email: string): Observable<boolean> {
        return this.http.get<boolean>(`${this.apiUrl}/check-email?email=${email}`);
    }

    logout(): void {
        this.http.post('http://localhost:8081/logout', {}, { withCredentials: true }).subscribe(() => {
            this.clearSession();
            this.router.navigate(['/login']);
        });
    }

    // Safe Session Management
    setSession(username: string, role: string): void {
        if (this.isBrowser) {
            sessionStorage.setItem('isLoggedIn', 'true');
            sessionStorage.setItem('username', username);
            sessionStorage.setItem('role', role);
        }
    }

    clearSession(): void {
        if (this.isBrowser) {
            sessionStorage.removeItem('isLoggedIn');
            sessionStorage.removeItem('username');
            sessionStorage.removeItem('role');
        }
    }

    isLoggedIn(): boolean {
        if (this.isBrowser) {
            return sessionStorage.getItem('isLoggedIn') === 'true';
        }
        return false;
    }

    getUsername(): string | null {
        if (this.isBrowser) {
            return sessionStorage.getItem('username');
        }
        return null;
    }
}
