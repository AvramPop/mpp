import {Injectable} from '@angular/core';
import {ActivatedRouteSnapshot, CanActivate, Router, RouterStateSnapshot} from '@angular/router';
import {LoginService} from './services/login/login.service';

@Injectable({providedIn: 'root'})
export class TeacherAuthGuard implements CanActivate {
  constructor(
    private router: Router,
    private loginService: LoginService
  ) {
  }

  canActivate(route: ActivatedRouteSnapshot, state: RouterStateSnapshot) {
    console.log("teacher guard" + this.loginService.currentUser);
    if (this.loginService.currentUser !== "ROLE_TEACHER") {
      this.router.navigateByUrl("/home");
      return false;
    } else {
      return true;
    }
  }
}
