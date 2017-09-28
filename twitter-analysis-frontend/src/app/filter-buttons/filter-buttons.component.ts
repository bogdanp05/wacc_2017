import { Component, OnInit } from '@angular/core';
import {TweetService} from '../tweet.service';

@Component({
  selector: 'app-filter-buttons',
  templateUrl: './filter-buttons.component.html',
  styleUrls: ['./filter-buttons.component.css']
})
export class FilterButtonsComponent implements OnInit {

  constructor(private tweetService:  TweetService) { }

  ngOnInit() {
  }

  filterGood() {
    this.tweetService.filterTweets(1);
  }

  filterNeutral() {
    this.tweetService.filterTweets(0);
  }

  filterBad() {
    this.tweetService.filterTweets(-1);
  }

  getTweets() {
    this.tweetService.filterAllTweets();
  }
}
