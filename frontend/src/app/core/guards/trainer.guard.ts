import { inject, PLATFORM_ID } from '@angular/core';
import { isPlatformBrowser } from '@angular/common';
import { Router, CanActivateFn } from '@angular/router';

export const trainerGuard: CanActivateFn = (route, state) => {
    const router = inject(Router);
    const platformId = inject(PLATFORM_ID);

    if (isPlatformBrowser(platformId)) {
        const role = localStorage.getItem('userRole');
        if (role === 'TRAINER' || role === 'ADMIN') {
            return true;
        }
    }

    router.navigate(['/auth/login']);
    return false;
};
