import { EtudiantService } from './etudiant.service';
import { Routes } from '@angular/router';
import { ListEtudiantComponent } from './list-etudiant/list-etudiant.component';
import { CreateEtudiantComponent } from './create-etudiant/create-etudiant.component';
import { EditEtudiantComponent } from './update-etudiant/update-etudiant.component';

export const routes: Routes = [
  { path: '', redirectTo: 'etudiants', pathMatch: 'full' },
  { path: 'etudiants', component: ListEtudiantComponent },
  { path: 'etudiants/create', component: CreateEtudiantComponent },
  { path: 'etudiants/edit/:matricule', component: EditEtudiantComponent }

];
