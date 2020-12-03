import { Component, OnInit } from '@angular/core';

import { AuthService } from '../auth/auth.service';
import { SignUpInfo } from '../auth/signup-info';
import {TokenStorageService} from '../auth/token-storage.service';

@Component({
  selector: 'app-register',
  templateUrl: './profile.component.html',
  styleUrls: ['./profile.component.css']
})
export class ProfileComponent implements OnInit {
  form: any = {};
  signupInfo;
  isSignedUp = false;
  isSignUpFailed = false;
  errorMessage = '';
  private user: string;

  constructor(private authService: AuthService,private tokenStorage: TokenStorageService) { }

  ngOnInit() {

    if (this.tokenStorage.getToken()) {
      this.user = this.tokenStorage.getUsername();
    }

    this.authService.getProfile(this.user).subscribe(
      data => {

        this.signupInfo = data;
        console.log(this.signupInfo);
        this.form.name = this.signupInfo.name;
        this.form.username = this.signupInfo.username;
      },
      error => {
        console.log(error);
      }
    );
  }

  onSubmit() {
    console.log(this.form);

    this.signupInfo.name = this.form.name;
    this.signupInfo.username = this.form.username;
    this.signupInfo.password = this.form.password;

    this.authService.update(this.signupInfo,this.signupInfo.id).subscribe(
      data => {
        console.log(data);
        this.isSignedUp = true;
        this.isSignUpFailed = false;
      },
      error => {
        console.log(error);
        this.errorMessage = error.error.message;
        this.isSignUpFailed = true;
      }
    );
  }
}
