import {Injectable} from '@angular/core';
import {ActivatedRouteSnapshot, CanActivate, Router, RouterStateSnapshot} from '@angular/router';
import {LoginService} from './services/login/login.service';

@Injectable({providedIn: 'root'})
export class AuthGuard implements CanActivate {
  constructor(
    private router: Router,
    private loginService: LoginService
  ) {
  }

  canActivate(route: ActivatedRouteSnapshot, state: RouterStateSnapshot) {
    console.log("guard" + this.loginService.currentUser);
    if (this.loginService.currentUser === "NONE") {
      this.router.navigateByUrl("/login");
      return false;
    } else {
      return true;
    }
  }
}
