import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

@Injectable({
    providedIn: 'root'
})
export class ReportService {
    private apiUrl = 'http://localhost:8081/api/reports';

    constructor(private http: HttpClient) { }

    downloadGradesPdf(studentId: number): Observable<Blob> {
        return this.http.get(`${this.apiUrl}/student/${studentId}/pdf`, {
            responseType: 'blob'
        });
    }
}
