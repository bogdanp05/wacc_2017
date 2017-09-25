import { Component, OnInit } from '@angular/core';

import { Tweet }                from '../tweet';
import { TweetService }         from '../tweet.service';

@Component({
  selector: 'app-filter-buttons',
  templateUrl: './filter-buttons.component.html',
  styleUrls: ['./filter-buttons.component.css'],
  providers: [TweetService]
})
export class FilterButtonsComponent implements OnInit {
  tweets: Tweet[];
  constructor(private tweetService: TweetService) { }

  getTweets(): void {
    this.tweetService
        .getTweets()
        .then(tweets => this.tweets = tweets);
  }

  ngOnInit() {
  //  this.getTweets();
  }

}
