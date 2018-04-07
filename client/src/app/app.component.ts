import {Component} from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {Router} from '@angular/router';
import 'rxjs/add/operator/finally';
import {AppService} from './app.service';
import {BaseUrlService} from "./base-url.service";

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css']
})
export class AppComponent {
  constructor(private baseUrlService: BaseUrlService, private app: AppService, private http: HttpClient, private router: Router) {
    this.app.authenticate(undefined, undefined);
  }

  logout() {
    this.http.post(this.baseUrlService.baseUrl + 'logout', {})
      .finally(() => {
        this.app.authenticated = false;
        this.router.navigateByUrl('/login');
      }).subscribe();
  }
}
