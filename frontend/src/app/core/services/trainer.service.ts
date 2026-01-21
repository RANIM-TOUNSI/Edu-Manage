import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '../../../environments/environment';
import { Trainer } from '../models/api.models';

@Injectable({
    providedIn: 'root'
})
export class TrainerService {
    private apiUrl = `${environment.apiUrl}/trainers`;

    constructor(private http: HttpClient) { }

    getAll(): Observable<Trainer[]> {
        return this.http.get<Trainer[]>(this.apiUrl);
    }

    getById(id: number): Observable<Trainer> {
        return this.http.get<Trainer>(`${this.apiUrl}/${id}`);
    }

    create(trainer: Trainer): Observable<Trainer> {
        return this.http.post<Trainer>(this.apiUrl, trainer);
    }

    update(id: number, trainer: Trainer): Observable<Trainer> {
        return this.http.put<Trainer>(`${this.apiUrl}/${id}`, trainer);
    }

    delete(id: number): Observable<void> {
        return this.http.delete<void>(`${this.apiUrl}/${id}`);
    }
}
