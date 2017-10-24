import { Tweet } from './tweet';
import { Injectable } from '@angular/core';
import { Headers, Http } from '@angular/http';
import { Observable } from 'rxjs';
import { Subject } from 'rxjs/Subject';
import {BehaviorSubject} from 'rxjs/BehaviorSubject';

import 'rxjs/add/operator/toPromise';

@Injectable()
export class TweetService {
  //private backendUrl = 'http://localhost:9000/bogdan';
  private backendUrl = 'http://' + window.location.hostname + ':9000/bogdan';
  private tweetsUrl = 'api/tweets';
  public subject = new Subject<any>();
  private tweets = new Array<Tweet>();
  private tweetsResult = new Array<Tweet>();
  private messageSource = new BehaviorSubject<any>('default sage');
  getMessageSource() {
    return this.messageSource.asObservable();
  }
  constructor(private http: Http) { }
  changeMessage(message: string) {
    console.log(message);
    this.messageSource.next(message);
  }

  getTweets()  {
    return this.tweets;
  }

  filterAllTweets() {
    this.subject.next(this.tweets);
  }

  filterTweets(quality: number) {
    this.tweetsResult = this.tweets.filter(tweet => tweet.analysis === quality);
    this.subject.next(this.tweetsResult);
  }

  getTweetsWithWord(word: string): Promise<Tweet[]> {

    const tweetsJSON = this.http.get('http://' + window.location.hostname + ':9000/getTweets/' + word)
      .toPromise()
      .then(response => response.json() as Tweet[])
      .catch(this.handleError);
    tweetsJSON.then(tweets => this.tweets = tweets);
    return tweetsJSON;
  }

  getNumberOfTweets(quality: number) {
    return this.tweets.filter(tweet => tweet.analysis === quality).length;
  }

  getNumberOfAllTweets() {
      return this.tweets.length;
  }

  getAmountOfTweetsByMonth(): Array<number> {

    for (const tweet of this.tweets) { //Solved that
      const date = new Date(tweet.timestamp);
      const days = date.getDay();
    }
    return [10, 5, 0, -10, 5, -5, 10, 12, -20, 40, 35, 0, 3];
  }

  private handleError(error: any): Promise<any> {
    console.error('An error occurred', error); // for demo purposes only
    return Promise.reject(error.message || error);
  }
}
