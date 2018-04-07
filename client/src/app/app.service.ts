import {Injectable} from '@angular/core';
import {HttpClient} from "@angular/common/http";
import {BaseUrlService} from "./base-url.service";
import {log} from "util";

@Injectable()
export class AppService {

  authenticated = false;

  constructor(private baseUrlService: BaseUrlService, private http: HttpClient) {
  }

  authenticate(credentials, callback) {

    // const headers = new HttpHeaders(credentials ? {
    //   authorization: 'Basic ' + btoa(credentials.username + ':' + credentials.password)
    // } : {});
    //
    // this.http.get(this.baseUrlService.baseUrl + 'user', {headers: headers}
    // ).subscribe(response => {
    //   if (response['name']) {
    //     this.authenticated = true;
    //   } else {
    //     this.authenticated = false;
    //   }
    //   return callback && callback();
    // });

    if (credentials) {
      log(credentials.username);
      log(credentials.password);
      this.http.post(this.baseUrlService.baseUrl + 'mlogin', {
        'login': credentials.username,
        'pass': credentials.password
      }).subscribe(response => {
        log('received result');
      });
    }
  }

  register(credentials: { username: string; password: string; email: string }, callback: () => void) {
    console.log(credentials.username, credentials.email, credentials.password);
    this.http.post(this.baseUrlService.baseUrl + 'user', credentials).subscribe(response => {
      if (callback) {
        callback();
      }
    });
  }
}
