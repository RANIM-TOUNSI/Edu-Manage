import { Injectable, Inject, PLATFORM_ID } from '@angular/core';
import { isPlatformBrowser } from '@angular/common';
import { HttpClient } from '@angular/common/http';
import { BehaviorSubject, Observable, tap } from 'rxjs';
import { environment } from '../../../environments/environment';
import { User } from '../models/api.models';

@Injectable({
    providedIn: 'root'
})
export class AuthService {
    private apiUrl = `${environment.apiUrl}/auth`;
    private currentUserSubject = new BehaviorSubject<User | null>(null);

    constructor(
        private http: HttpClient,
        @Inject(PLATFORM_ID) private platformId: Object
    ) {
        if (isPlatformBrowser(this.platformId)) {
            const savedUser = localStorage.getItem('currentUser');
            if (savedUser) {
                this.currentUserSubject.next(JSON.parse(savedUser));
            }
        }
    }

    login(credentials: any): Observable<any> {
        return this.http.post<any>(`${this.apiUrl}/login`, credentials).pipe(
            tap(response => {
                if (isPlatformBrowser(this.platformId)) {
                    localStorage.setItem('token', response.token);
                    localStorage.setItem('currentUser', JSON.stringify(response.user));
                    localStorage.setItem('userRole', response.user.role);
                }
                this.currentUserSubject.next(response.user);
            })
        );
    }

    register(userData: any): Observable<any> {
        return this.http.post<any>(`${this.apiUrl}/register`, userData);
    }

    logout() {
        if (isPlatformBrowser(this.platformId)) {
            localStorage.clear();
        }
        this.currentUserSubject.next(null);
    }

    get currentUser(): Observable<User | null> {
        return this.currentUserSubject.asObservable();
    }

    getToken(): string | null {
        if (isPlatformBrowser(this.platformId)) {
            return localStorage.getItem('token');
        }
        return null;
    }
}
