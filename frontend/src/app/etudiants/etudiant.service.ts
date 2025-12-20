import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Etudiant } from '../models/etudiant.model';

@Injectable({ providedIn: 'root' })
export class EtudiantService {

  private apiUrl = 'http://localhost:8081/api/etudiants';

  constructor(private http: HttpClient) {}

  getAll(): Observable<Etudiant[]> {
    return this.http.get<Etudiant[]>(this.apiUrl);
  }

  create(etudiant: Etudiant): Observable<Etudiant> {
    return this.http.post<Etudiant>(this.apiUrl, etudiant);
  }

delete(matricule: string): Observable<void> {
  return this.http.delete<void>(`${this.apiUrl}/${matricule}`);
}
update(matricule: string, etudiant: Etudiant): Observable<Etudiant> {
  return this.http.put<Etudiant>(
    `${this.apiUrl}/${matricule}`,
    etudiant
  );
}


}
