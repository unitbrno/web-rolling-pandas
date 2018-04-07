import {Component, OnInit} from '@angular/core';
import {AppService} from "../app.service";
import {HttpClient} from "@angular/common/http";
import {BaseUrlService} from "../base-url.service";

@Component({
  selector: 'app-home',
  templateUrl: './home.component.html',
  styleUrls: ['./home.component.css']
})
export class HomeComponent implements OnInit {

  title = 'Demo';
  greeting = {};

  constructor(private baseUrlService: BaseUrlService, private app: AppService, private http: HttpClient) {
    http.get(baseUrlService.baseUrl + 'resource').subscribe(data => this.greeting = data);
  }

  ngOnInit() {
  }

  authenticated() {
    return this.app.authenticated;
  }

}
