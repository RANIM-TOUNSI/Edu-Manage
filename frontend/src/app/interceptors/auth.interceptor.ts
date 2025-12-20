import { HttpInterceptorFn } from '@angular/common/http';

export const authInterceptor: HttpInterceptorFn = (req, next) => {
    // Check if request is for our backend API
    if (req.url.includes('http://localhost:8081')) {
        const clonedReq = req.clone({
            withCredentials: true
        });
        return next(clonedReq);
    }
    return next(req);
};
