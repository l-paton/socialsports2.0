import { Injectable } from "@angular/core";
import { HttpInterceptor, HttpRequest, HttpHandler } from "@angular/common/http";
import { AuthService } from "../services/auth.service";
import { TokenStorageService } from '../services/token-storage.service'

@Injectable({
    providedIn: 'root'
  })
export class AuthInterceptor implements HttpInterceptor {
    constructor(private authService: AuthService, private tokenStorage: TokenStorageService) { }

    intercept(req: HttpRequest<any>, next: HttpHandler) {
        const authToken = this.tokenStorage.getToken();
        req = req.clone({
            setHeaders: {
                Authorization: "Bearer " + authToken
            }
        });
        return next.handle(req);
    }
}