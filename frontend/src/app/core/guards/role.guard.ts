import { Injectable } from '@angular/core';
import { CanActivate, ActivatedRouteSnapshot, RouterStateSnapshot, Router } from '@angular/router';

@Injectable({
    providedIn: 'root'
})
export class RoleGuard implements CanActivate {
    constructor(private router: Router) { }

    canActivate(route: ActivatedRouteSnapshot, state: RouterStateSnapshot): boolean {
        const expectedRoles = route.data['roles'] as Array<string>;
        const userRole = localStorage.getItem('userRole'); // Simple implementation for structure

        if (!userRole || !expectedRoles.includes(userRole)) {
            this.router.navigate(['/auth/login']);
            return false;
        }
        return true;
    }
}
