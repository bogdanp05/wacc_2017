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

import { Component, OnInit } from '@angular/core';

import { Tweet } from '../tweet';
import { TweetService } from '../tweet.service';

@Component({
  selector: 'tweets-list',
  templateUrl: './tweets-list.component.html',
  styleUrls: ['./tweets-list.component.css'],
  providers: [TweetService]
})
export class TweetsListComponent implements OnInit {
  tweets: Tweet[];
  selectedTweet: Tweet;

  constructor(
  //  private router: Router,
    private tweetService: TweetService) { }

  getTweets(): void {
    this.tweetService.getTweets().then(tweets => this.tweets = tweets);
  }

  ngOnInit(): void {
    this.getTweets();
  }

  onSelect(tweet: Tweet): void {
    this.selectedTweet = tweet;
  }

  filterGood(): void {
    this.tweetService
        .filterTweets(1)
        .then(tweets => this.tweets = tweets);
  }

  filterNeutral(): void {
    this.tweetService
        .filterTweets(0)
        .then(tweets => this.tweets = tweets);
  }

  filterBad(): void {
    this.tweetService
        .filterTweets(-1)
        .then(tweets => this.tweets = tweets);
  }

  analysisClass(tweet: Tweet): string {
    if (tweet.analysis == -1){
      return 'bad';
    }
    if (tweet.analysis == 1){
      return 'good';
    }
    return 'neutral';
  }
  //
  // gotoDetail(): void {
  //   this.router.navigate(['/detail', this.selectedHero.id]);
  // }
}
