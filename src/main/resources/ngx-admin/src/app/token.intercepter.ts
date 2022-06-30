import { Inject, Injectable, Injector } from '@angular/core';
import { HttpEvent, HttpHandler, HttpInterceptor, HttpRequest } from '@angular/common/http';
import { Observable } from 'rxjs/Observable';
import { NbAuthService, NbAuthToken, NB_AUTH_TOKEN_INTERCEPTOR_FILTER } from '@nebular/auth';
import { catchError, switchMap } from 'rxjs/operators';
import { NbGlobalPhysicalPosition, NbToastrService } from '@nebular/theme';
import { throwError } from 'rxjs';

/**
 * TokenInterceptor
 * @see https://angular.io/guide/http#intercepting-all-requests-or-responses
 */
@Injectable()
export class TokenInterceptor implements HttpInterceptor {
    constructor(private injector: Injector,
        private toastService: NbToastrService,
        @Inject(NB_AUTH_TOKEN_INTERCEPTOR_FILTER) protected filter) {
    }
    intercept(req: HttpRequest<any>, next: HttpHandler): Observable<HttpEvent<any>> {
        // do not intercept request whose urls are filtered by the injected filter
        if (!this.filter(req)) {
            return this.authService.isAuthenticatedOrRefresh()
                .pipe(
                    switchMap(authenticated => {
                        if (authenticated) {
                            return this.authService.getToken().pipe(
                                switchMap((token: NbAuthToken) => {
                                    const JWT = `Bearer ${token.getValue()}`;
                                    req = req.clone({
                                        setHeaders: {
                                            Authorization: JWT,
                                        },
                                    });
                                    return next.handle(req).pipe(
                                        catchError(error => {
                                            this.toastService.show(error.error?.detail, error.error?.title, {
                                                position: NbGlobalPhysicalPosition.TOP_RIGHT,
                                                duration: 3000,
                                                status: error?.error?.type ?? 'warning',
                                            });
                                            return throwError(error);
                                        }),
                                    );
                                }),
                            );
                        } else {
                            // Request is sent to server without authentication so that the client code
                            // receives the 401/403 error and can act as desired ('session expired', redirect to login)
                            return next.handle(req);
                        }
                    }),
                );
        } else {
            return next.handle(req);
        }
    }

    protected get authService(): NbAuthService {
        return this.injector.get(NbAuthService);
    }
}
