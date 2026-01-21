import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Seance } from '../models/api.models';

@Injectable({
    providedIn: 'root'
})
export class PlanningService {
    private apiUrl = 'http://localhost:8081/api/planning';

    constructor(private http: HttpClient) { }

    getMySchedule(): Observable<Seance[]> {
        return this.http.get<Seance[]>(`${this.apiUrl}/my-schedule`);
    }

    getTrainerSchedule(trainerId: number): Observable<Seance[]> {
        return this.http.get<Seance[]>(`${this.apiUrl}/trainer/${trainerId}`);
    }
}
