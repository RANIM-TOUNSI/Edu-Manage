import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '../../../environments/environment';
import { Grade } from '../models/api.models';

@Injectable({
    providedIn: 'root'
})
export class GradeService {
    private apiUrl = `${environment.apiUrl}/grades`;

    constructor(private http: HttpClient) { }

    assign(studentId: number, courseId: number, value: number): Observable<Grade> {
        return this.http.post<Grade>(this.apiUrl, { studentId, courseId, value });
    }

    getByStudent(studentId: number): Observable<Grade[]> {
        return this.http.get<Grade[]>(`${this.apiUrl}/student/${studentId}`);
    }
}
