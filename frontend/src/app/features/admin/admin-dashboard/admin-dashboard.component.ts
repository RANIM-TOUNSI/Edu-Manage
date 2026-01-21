import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { StudentService } from '../../../core/services/student.service';
import { CourseService } from '../../../core/services/course.service';
import { TrainerService } from '../../../core/services/trainer.service';
import { RouterModule } from '@angular/router';

@Component({
    selector: 'app-admin-dashboard',
    standalone: true,
    imports: [CommonModule, RouterModule],
    templateUrl: './admin-dashboard.component.html',
    styleUrls: ['./admin-dashboard.component.scss']
})
export class AdminDashboardComponent implements OnInit {
    stats = { students: 0, trainers: 0, courses: 0 };

    constructor(
        private studentService: StudentService,
        private courseService: CourseService,
        private trainerService: TrainerService
    ) { }

    ngOnInit() {
        this.studentService.getAll().subscribe((data: any[]) => this.stats.students = data.length);
        this.trainerService.getAll().subscribe((data: any[]) => this.stats.trainers = data.length);
        this.courseService.getAll().subscribe((data: any[]) => this.stats.courses = data.length);
    }
}
