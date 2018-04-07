import { Injectable } from '@angular/core';
import { Observable } from 'rxjs/Observable';
import { catchError, map, tap } from 'rxjs/operators';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Place } from './place'

const httpOptions = {
  headers: new HttpHeaders({ 'Content-Type': 'application/json' })
};

@Injectable()
export class PlaceService {

  private placeUrl = 'http://localhost:8080/brno/';


  constructor(private http: HttpClient) {

  }
  getPlaces(name : string) : Observable<Place[]> {
    return this.http.get<Place[]>(this.placeUrl)
      .pipe(tap(places => console.log(`fetched places`)));
  }
}
