import { Component, OnInit } from '@angular/core';
import { MatSnackBar } from '@angular/material/snack-bar';
import { TranslateService } from '@ngx-translate/core';
import { AuthService } from './features/auth/services/auth.service';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.scss']
})
export class AppComponent implements OnInit {
  
  constructor(private translate: TranslateService, private authService: AuthService, private _snackBar: MatSnackBar) {
    translate.addLangs(['en', 'pl']);
    this.authService.initUserInfo();
  }

  ngOnInit(): void {
    this.authService.error$.subscribe(error => {
      const errorMessage = error as string;
      if (errorMessage === "Cannot access") {
        this.translate.get("AUTH.ACCESS").subscribe(translation => {
          this._snackBar.open(translation, "OK", {duration: 5000})
        })
      } else {
        this._snackBar.open(errorMessage, "OK", {duration: 5000})
      }
      
    });
  }
}
