import { HttpInterceptorFn } from '@angular/common/http';
import { inject } from '@angular/core';
import { from, switchMap } from 'rxjs';
import { environment } from '../../../environments/environment';
import { AuthService } from '../auth/auth.service';

export const authInterceptor: HttpInterceptorFn = (req, next) => {
  const authService = inject(AuthService);
  const isApiCall = req.url.startsWith('http') ? req.url.startsWith(environment.apiBaseUrl) : true;

  if (!isApiCall) {
    return next(req);
  }

  return from(authService.ensureValidToken()).pipe(
    switchMap(() => {
      const token = authService.getToken();
      if (!token) {
        return next(req);
      }
      return next(
        req.clone({
          setHeaders: {
            Authorization: `Bearer ${token}`
          }
        })
      );
    })
  );
};
