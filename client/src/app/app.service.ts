import {Injectable} from '@angular/core';
import {HttpClient} from "@angular/common/http";
import {BaseUrlService} from "./base-url.service";

@Injectable()
export class AppService {

  authenticated = false;

  constructor(private baseUrlService: BaseUrlService, private http: HttpClient) {
  }
  authenticate(credentials, callback) {
    this.http.get(this.baseUrlService.baseUrl + 'user', {}
    ).subscribe(response => {
      if (response) {
        console.log('authenticated');
        this.authenticated = true;
      } else {
        console.log('NOT authenticated');
        this.authenticated = false;
      }
      return callback && callback();
    });
  }

  register(credentials: { username: string; password: string; email: string }, callback: () => void) {
    console.log(credentials.username, credentials.email, credentials.password);
    this.http.post(this.baseUrlService.baseUrl + 'register', credentials).subscribe(response => {
      if (callback) {
        callback();
      }
    });
  }
}
