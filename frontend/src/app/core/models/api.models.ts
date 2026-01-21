export interface User {
    id?: number;
    username: string;
    role: 'ADMIN' | 'TRAINER' | 'STUDENT';
    trainerId?: number;  // ID of the trainer record if user is a TRAINER
    studentId?: number;  // ID of the student record if user is a STUDENT
}

export interface Student {
    id?: number;
    matricule: string;
    firstName: string;
    lastName: string;
    email: string;
    registrationDate?: string;
}

export interface Trainer {
    id?: number;
    name: string;
    specialty?: string;
    email: string;
}

export interface Course {
    id?: number;
    code: string;
    title: string;
    description?: string;
    trainerId?: number;
    trainerName?: string;
}

export interface Enrollment {
    id?: number;
    enrollmentDate: string;
    studentId: number;
    courseId: number;
}

export interface Grade {
    id?: number;
    value: number;
    studentId: number;
    courseId: number;
    courseTitle?: string;
}

export interface Specialty {
    id?: number;
    name: string;
    description?: string;
}

export interface StudentGroup {
    id?: number;
    name: string;
    specialtyId: number;
}

export interface Session {
    id?: number;
    title: string;
    startDate: string;
    endDate: string;
    active: boolean;
}

export interface Seance {
    id?: number;
    courseId: number;
    courseTitle: string;
    groupId: number;
    groupName: string;
    date: string;
    startTime: string;
    endTime: string;
    room: string;
}

