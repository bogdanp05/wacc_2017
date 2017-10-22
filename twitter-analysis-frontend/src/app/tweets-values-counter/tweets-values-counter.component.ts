import { Component, OnInit } from '@angular/core';
import {TweetService} from '../tweet.service';

@Component({
  selector: 'app-tweets-values-counter',
  templateUrl: './tweets-values-counter.component.html',
  styleUrls: ['./tweets-values-counter.component.css']
})
export class TweetsValuesCounterComponent implements OnInit {

  constructor(private tweetService:  TweetService) { }

  ngOnInit() {
  }

  getNumberOfTweets(quality: number) {
    return this.tweetService.getNumberOfTweets(quality);
  }

  getNumberOfAllTweets() {
    return this.tweetService.getNumberOfAllTweets();
  }


}
