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

  getTweets(): Promise<Tweet[]> {
    this.http.get(this.backendUrl) // I'm adding this to test the connecttion to the backend without db
      .toPromise()
      .then (function(response){
        console.log(response);
      });
    const tweetsJSON = this.http.get('http://' + window.location.hostname + ':9000/getAllTweets')
      .toPromise()
      .then(response => response.json() as Tweet[])
      .catch(this.handleError);
    tweetsJSON.then(tweets => this.tweets = tweets);
    return tweetsJSON;
  }

  filterAllTweets() {
    this.subject.next(this.tweets);
  }

  filterTweets(quality: number) {
    this.tweetsResult = this.tweets.filter(tweet => tweet.analysis === quality);
    this.subject.next(this.tweetsResult);
  }

  filterTweetsWithText(text: string) {
    this.tweetsResult = this.tweets.filter(tweet => tweet.content.includes(text));
    this.subject.next(this.tweetsResult);
  }

  getNumberOfTweets(quality: number) {
    return this.tweets.filter(tweet => tweet.analysis === quality).length;
  }

  getNumberOfAllTweets() {
      return this.tweets.length
  }

  private handleError(error: any): Promise<any> {
    console.error('An error occurred', error); // for demo purposes only
    return Promise.reject(error.message || error);
  }
}
