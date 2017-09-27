import { Tweet } from './tweet';
import { Injectable } from '@angular/core';
import { Headers, Http } from '@angular/http';

import 'rxjs/add/operator/toPromise';

@Injectable()
export class TweetService {
  private tweetsUrl = 'api/tweets';
  constructor(private http: Http) { }

  getTweets(): Promise<Tweet[]> {
    // return Promise.resolve(TWEETS);
    return this.http.get(this.tweetsUrl)
          .toPromise()
          .then(response => response.json().data as Tweet[])
          .catch(this.handleError);
  }

  filterTweets(quality: number): Promise<Tweet[]> {
    return this.http.get(this.tweetsUrl)
          .toPromise()
          .then(response => response.json().data as Tweet[])
          .then(tweets => tweets.filter(tweet => tweet.analysis === quality))
          .catch(this.handleError);
  }

  private handleError(error: any): Promise<any> {
    console.error('An error occurred', error); // for demo purposes only
    return Promise.reject(error.message || error);
  }
}
