import { Tweet } from './tweet';
// import { TWEETS } from './mock-tweets';
import { Injectable } from '@angular/core';
import { Headers, Http } from '@angular/http';
import {BehaviorSubject} from 'rxjs/BehaviorSubject';

import 'rxjs/add/operator/toPromise';

@Injectable()
export class TweetService {
  private tweetsUrl = 'api/tweets';
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
    // return Promise.resolve(TWEETS);
    return this.http.get(this.tweetsUrl)
          .toPromise()
          .then(response => response.json().data as Tweet[])
          .catch(this.handleError);
  }

  // getHeroesSlowly(): Promise<Hero[]> {
  //   return new Promise(resolve => {
  //     // Simulate server latency with 2 second delay
  //     setTimeout(() => resolve(this.getHeroes()), 2000);
  //   });
  // }

  // getTweet(id: number): Promise<Tweet> {
  //   return this.getTweets()
  //              .then(tweets => tweets.find(tweet => tweet.id === id));
  // }

  filterTweets(quality: number): Promise<Tweet[]>{
    // return this.getTweets()
    //            .then(tweets => tweets.filter(tweet => tweet.analysis=== quality));
    return this.http.get(this.tweetsUrl)
          .toPromise()
          .then(response => response.json().data as Tweet[])
          .then(tweets => tweets.filter(tweet => tweet.analysis=== quality))
          .catch(this.handleError);
  }

  searchTweets(searchText: string): Promise<Tweet[]> {
    // return this.getTweets()
    //            .then(tweets => tweets.filter(tweet => tweet.analysis=== quality));
    return this.http.get(this.tweetsUrl)
      .toPromise()
      .then(response => response.json().data as Tweet[])
      .then(tweets => tweets.filter(tweet => tweet.text.indexOf(searchText) > -1))
      .catch(this.handleError);
  }

  private handleError(error: any): Promise<any> {
    console.error('An error occurred', error); // for demo purposes only
    return Promise.reject(error.message || error);
  }
}
