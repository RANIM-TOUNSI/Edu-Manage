import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterModule } from '@angular/router';
import { EtudiantService } from '../etudiant.service';
import { Etudiant } from '../../models/etudiant.model';

@Component({
  standalone: true,
  selector: 'app-list-etudiant',
  templateUrl: './list-etudiant.component.html',
  imports: [CommonModule, RouterModule]
})
export class ListEtudiantComponent implements OnInit {

  etudiants: Etudiant[] = [];

  constructor(private service: EtudiantService) {}

  ngOnInit(): void {
    this.service.getAll().subscribe({
      next: data => this.etudiants = data,
      error: err => console.error(err)
    });
  }

  delete(matricule: string) {
    this.service.delete(matricule).subscribe(() => {
      this.etudiants = this.etudiants.filter(e => e.matricule !== matricule);
    });
  }
}
