import { Injectable } from '@angular/core';
import { HttpEvent, HttpHandler, HttpInterceptor, HttpRequest } from '@angular/common/http';
import { Observable } from 'rxjs';
import { LoginService } from './login/login.service';

@Injectable()
export class AuthInterceptor implements HttpInterceptor {
    constructor(private ls:LoginService) { }

    getToken(): string | boolean {
        const currentUser = this.ls.retriveUserData();
        return currentUser && currentUser.token;
    }

    intercept(req: HttpRequest<any>, next: HttpHandler): Observable<HttpEvent<any>> {
        if (this.getToken()) {
            req = req.clone({
                setHeaders: {
                    Authorization: 'Bearer ' + this.getToken()
                }
            });
        }
        return next.handle(req);
    }
}