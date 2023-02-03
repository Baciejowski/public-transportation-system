import {Component, OnInit, ViewChild} from '@angular/core';
import { faBars } from '@fortawesome/free-solid-svg-icons';
import { faChevronLeft } from '@fortawesome/free-solid-svg-icons';
import jwtDecode from 'jwt-decode';
import { JwtDto } from 'src/app/features/auth/models/jwtDto';
import { AuthService } from 'src/app/features/auth/services/auth.service';

@Component({
  selector: 'app-menu',
  templateUrl: './menu.component.html',
  styleUrls: ['./menu.component.scss']
})
export class MenuComponent implements OnInit {

  hasToken: boolean;

  constructor(private authService: AuthService) { }

  ngOnInit(): void {
    this.hasToken = !!this.authService.session.getRefreshToken();
  }

  checkRole(allowedRoles: string[]): boolean {
    const role = (jwtDecode(this.authService.session.getRefreshToken()!) as JwtDto).role;
    if (allowedRoles.includes(role)) {
      return true;
    }
    return false
  }

  faBars = faBars;
  faChevronLeft = faChevronLeft
}
