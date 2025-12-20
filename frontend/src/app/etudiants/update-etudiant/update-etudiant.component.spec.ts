import { ComponentFixture, TestBed } from '@angular/core/testing';

import { EditEtudiantComponent  } from './update-etudiant.component';

describe('UpdateEtudiantComponent', () => {
  let component: EditEtudiantComponent ;
  let fixture: ComponentFixture<EditEtudiantComponent >;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [EditEtudiantComponent ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(EditEtudiantComponent );
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
