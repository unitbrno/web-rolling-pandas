import { Component, OnInit } from '@angular/core';
import { PLACES } from '../place-mock'
import { Place } from '../place'
import { PlaceService } from '../place.service'


@Component({
  selector: 'app-places',
  templateUrl: './places.component.html',
  styleUrls: ['./places.component.css']
})


export class PlacesComponent implements OnInit {
  searchTerm : string = "";
  places : Place[];

  constructor(private placeService: PlaceService) { }

  ngOnInit() {
    this.placeService.getPlaces("Yacht").subscribe(places => this.places = places);
  }
}
