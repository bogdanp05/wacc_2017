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
    this.tweets = this.tweetService.getTweets();
  }

  ngOnInit(): void {
    this.tweetService.subject.subscribe(
      goodTweets => this.tweets = goodTweets
    );
    console.log(this.tweets)
    this.getTweets();
  }

  analysisClass(analysis: number): string {
    if (analysis === -1) {
      return 'bad';
    }
    if (analysis === 1) {
      return 'good';
    }
    return 'neutral';
  }

  getDate(timestamp: number): string {
    const date = new Date(timestamp);

    let days = date.getDay().toString();
    if (date.getDay() < 10) {
      days = '0' + days;
    }

    let months = date.getMonth().toString();
    if (date.getMonth() < 10) {
      months = '0' + months;
    }

    let hours = date.getHours().toString();
    if (date.getHours() < 10) {
      hours = '0' + hours;
    }
    let minutes = date.getMinutes().toString();
    if (date.getMinutes() < 10) {
      minutes = '0' + minutes;
    }



// Will display time in 10:30:23 format
    return days + '/' + months + '/' + date.getFullYear() + ' ' + hours + ':' + minutes;
  }
}
