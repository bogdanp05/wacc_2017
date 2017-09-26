// import { Component, OnInit } from '@angular/core';
//
// @Component({
//   selector: 'app-tweets-list',
//   templateUrl: './tweets-list.component.html',
//   styleUrls: ['./tweets-list.component.css']
// })
// export class TweetsListComponent implements OnInit {
//
//   constructor() { }
//
//   ngOnInit() {
//   }
//
// }

import {Component, OnInit, SimpleChanges, OnChanges} from '@angular/core';

import { Tweet } from '../tweet';
import { TweetService } from '../tweet.service';

@Component({
  selector: 'tweets-list',
  templateUrl: './tweets-list.component.html',
  styleUrls: ['./tweets-list.component.css'],
  providers: [TweetService]
})
export class TweetsListComponent implements OnInit, OnChanges {
  tweets: Tweet[];
  selectedTweet: Tweet;
  filterIndex= 99;
  searchText: string;
  ngOnChanges(changes: SimpleChanges) {
    console.log(changes.searchText);
    if (!changes.searchText.firstChange) {
      if (changes.searchText.currentValue.length > 0) {
        this.tweetService.searchTweets(this.searchText)
          .then(tweets => this.tweets = tweets);
      }else {
        this.getTweets();
      }
    }
  }

  constructor(
  //  private router: Router,
    private tweetService: TweetService) { }

  getTweets(): void {
    this.filterIndex = 99;
    this.tweetService.getTweets().then(tweets => this.tweets = tweets);
  }


  ngOnInit(): void {
    console.log('jjj')
    this.tweetService.getMessageSource().subscribe(val => {
      this.searchText = val;
      console.log(val);
    });
    this.getTweets();
  }

  onSelect(tweet: Tweet): void {
    this.selectedTweet = tweet;
  }

  filterGood(): void {
    this.filterIndex = 1;
    this.tweetService
        .filterTweets(this.filterIndex)
        .then(tweets => this.tweets = tweets);
  }

  filterNeutral(): void {
    this.filterIndex = 0;
    this.tweetService
        .filterTweets(this.filterIndex)
        .then(tweets => this.tweets = tweets);
  }

  filterBad(): void {
    this.filterIndex = -1;
    this.tweetService
        .filterTweets(this.filterIndex)
        .then(tweets => this.tweets = tweets);
  }

  analysisClass(tweet: Tweet): string {
    if (tweet.analysis == -1) {
      return 'bad';
    }
    if (tweet.analysis == 1) {
      return 'good';
    }
    return 'neutral';
  }

  //
  // gotoDetail(): void {
  //   this.router.navigate(['/detail', this.selectedHero.id]);
  // }
}
