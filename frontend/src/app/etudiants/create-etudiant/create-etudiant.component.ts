import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ReactiveFormsModule, FormBuilder, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import { EtudiantService } from '../etudiant.service';
import { Etudiant } from '../../models/etudiant.model';

@Component({
  selector: 'app-create-etudiant',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule],
  templateUrl: './create-etudiant.component.html'
})
export class CreateEtudiantComponent {

  form = this.fb.group({
    matricule: ['', Validators.required],
    nom: ['', Validators.required],
    prenom: ['', Validators.required],
    email: ['', [Validators.required, Validators.email]],
  });

  constructor(
    private fb: FormBuilder,
    private service: EtudiantService,
    private router: Router
  ) {}

  submit() {
    if (this.form.valid) {
      // appel au backend pour créer l'étudiant
      this.service.create(this.form.value as Etudiant).subscribe({
        next: () => this.router.navigate(['/etudiants']),
        error: err => console.error('Erreur création étudiant', err)
      });
    } else {
      this.form.markAllAsTouched();
    }
  }
}
