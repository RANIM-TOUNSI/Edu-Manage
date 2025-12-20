import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormBuilder, ReactiveFormsModule, Validators } from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';
import { EtudiantService } from '../etudiant.service';
import { Etudiant } from '../../models/etudiant.model';

@Component({
  selector: 'app-edit-etudiant',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule],
  templateUrl: './update-etudiant.component.html'
})
export class EditEtudiantComponent implements OnInit {

  matricule!: string;

  form = this.fb.nonNullable.group({
    nom: ['', Validators.required],
    prenom: ['', Validators.required],
    email: ['', [Validators.required, Validators.email]]
  });

  constructor(
    private fb: FormBuilder,
    private service: EtudiantService,
    private route: ActivatedRoute,
    private router: Router
  ) {}

  ngOnInit(): void {
    const m = this.route.snapshot.paramMap.get('matricule');
    if (!m) {
      alert('Matricule manquant');
      return;
    }

    this.matricule = m;

    this.service.getAll().subscribe(etudiants => {
      const e = etudiants.find(x => x.matricule === this.matricule);
      if (e) {
        this.form.patchValue(e);
      }
    });
  }

  submit(): void {
    if (this.form.invalid) return;

    const etudiant: Etudiant = {
      matricule: this.matricule,
      ...this.form.getRawValue(),
      dateInscription: new Date().toISOString()
    };

    this.service.update(this.matricule, etudiant).subscribe(() => {
      this.router.navigate(['/etudiants']);
    });
  }

  goToList(): void {
    this.router.navigate(['/etudiants']);
  }
}
