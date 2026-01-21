import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '../../../environments/environment';
import { Enrollment } from '../models/api.models';

@Injectable({
    providedIn: 'root'
})
export class EnrollmentService {
    private apiUrl = `${environment.apiUrl}/enrollments`;

    constructor(private http: HttpClient) { }

    enroll(studentId: number, courseId: number): Observable<Enrollment> {
        return this.http.post<Enrollment>(this.apiUrl, { studentId, courseId });
    }

    cancel(id: number): Observable<void> {
        return this.http.delete<void>(`${this.apiUrl}/${id}`);
    }

    getByStudent(studentId: number): Observable<Enrollment[]> {
        return this.http.get<Enrollment[]>(`${this.apiUrl}/student/${studentId}`);
    }
}
