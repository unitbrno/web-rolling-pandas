import {Component, OnInit} from '@angular/core';
import {AppService} from "../app.service";
import {HttpClient} from "@angular/common/http";
import {Router} from "@angular/router";

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['../css/theme.css']
})
export class LoginComponent implements OnInit {

  credentials = {username: '', password: '', email: ''};

  constructor(private appService: AppService, private http: HttpClient, private router: Router) {
  }

  ngOnInit() {
  }

  register() {
    this.appService.register(this.credentials, () => {
      this.router.navigateByUrl('/');
    });
    return false;
  }

}
