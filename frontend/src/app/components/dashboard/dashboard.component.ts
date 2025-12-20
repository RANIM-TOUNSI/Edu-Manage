import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { HttpClient } from '@angular/common/http';
import { AuthService } from '../../services/auth.service';

@Component({
    selector: 'app-dashboard',
    standalone: true,
    imports: [CommonModule],
    templateUrl: './dashboard.component.html'
})
export class StudentDashboardComponent implements OnInit {
    courses: any[] = [];
    myNotes: any[] = [];
    myInscriptions: Set<string> = new Set(); // Set of Course Codes

    // Note: In a real app complexity, these calls would be in separate services.
    // For 'Keep it simple', we'll put some logic here or use simple service calls.
    private apiUrl = 'http://localhost:8081/api';

    constructor(private http: HttpClient, public authService: AuthService) { }

    ngOnInit(): void {
        this.loadCourses();
        this.loadNotes();
        // Ideally load inscriptions too to check what is already registered
    }

    loadCourses() {
        this.http.get<any[]>(`${this.apiUrl}/cours`, { withCredentials: true }).subscribe(data => {
            this.courses = data;
        });
    }

    loadNotes() {
        this.http.get<any[]>(`${this.apiUrl}/notes`, { withCredentials: true }).subscribe(data => {
            this.myNotes = data;
        });
    }

    register(courseCode: string) {
        const matricule = this.authService.getUsername(); // Safe access
        if (!matricule) {
            alert('User not identified');
            return;
        }
        // And our InscriptionRestController.register() takes InscriptionInput.
        // Wait, the API endpoint /api/inscriptions uses @RequestBody InscriptionInput.
        // We need to send correct data.

        // Simplification: Using current date and stored username.
        const body = {
            date: new Date().toISOString().split('T')[0],
            etudiantMatricule: matricule,
            coursCode: courseCode
        };

        this.http.post(`${this.apiUrl}/inscriptions`, body, { withCredentials: true }).subscribe({
            next: () => {
                alert('Registered successfully!');
                this.myInscriptions.add(courseCode);
            },
            error: (err) => alert('Registration failed: ' + err.error?.message || 'Error')
        });
    }

    unregister(inscriptionId: number) {
        // We need inscription ID. This details would typically come from a /my-inscriptions endpoint which we haven't built explicitly in REST API, 
        // but we can infer or maybe we just don't implement unregister for this MVP from the course list if we don't have the ID handy.
        // Or we can implementing it if we had the ID.
        // For now, let's just show Register.
    }
}
