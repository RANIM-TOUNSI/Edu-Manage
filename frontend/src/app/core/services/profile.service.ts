import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { User } from '../models/api.models';

@Injectable({
    providedIn: 'root'
})
export class ProfileService {
    private apiUrl = 'http://localhost:8081/api/profile';

    constructor(private http: HttpClient) { }

    getProfile(): Observable<User> {
        return this.http.get<User>(this.apiUrl);
    }

    changePassword(newPassword: string): Observable<void> {
        const params = { newPassword };
        return this.http.post<void>(`${this.apiUrl}/change-password`, null, { params });
    }
}
