import { Component, OnInit } from '@angular/core';
import { Tweet } from '../tweet';
import { TweetService } from '../tweet.service';

@Component({
  selector: 'app-tweets-table',
  templateUrl: './tweets-table.component.html',
  styleUrls: ['./tweets-table.component.css']
})
export class TweetsTableComponent implements OnInit {
  tweets: Tweet[];
  selectedTweet: Tweet;

  constructor(
    //  private router: Router,
    private tweetService: TweetService) { }

  getTweets(): void {
    this.tweetService.getTweets().then(tweets => this.tweets = tweets);
  }

  ngOnInit(): void {
    this.tweetService.subject.subscribe(
      goodTweets => this.tweets = goodTweets
    );
    console.log(this.tweets)
    this.getTweets();
  }

  analysisClass(tweet: Tweet): string {
    if (tweet.analysis === -1) {
      return 'bad';
    }
    if (tweet.analysis === 1) {
      return 'good';
    }
    return 'neutral';
  }
}
