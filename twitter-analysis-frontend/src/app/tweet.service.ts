import { Tweet } from './tweet';
import { Injectable } from '@angular/core';
import { Headers, Http } from '@angular/http';
import { Observable } from 'rxjs';
import { Subject } from 'rxjs/Subject';

import 'rxjs/add/operator/toPromise';

@Injectable()
export class TweetService {
  private tweetsUrl = 'api/tweets';
  public subject = new Subject<any>();
  private tweetsResult = Array<Tweet>();
  constructor(private http: Http) { }

  getTweets(): Promise<Tweet[]> {
    // return Promise.resolve(TWEETS);
    return this.http.get(this.tweetsUrl)
          .toPromise()
          .then(response => response.json().data as Tweet[])
          .catch(this.handleError);
  }

  filterAllTweets() {
    this.http.get(this.tweetsUrl)
      .toPromise()
      .then(response => response.json().data as Tweet[])
      .then(tweets => this.subject.next(tweets))
      .catch(this.handleError);
  }

  filterTweets(quality: number) {

    this.http.get(this.tweetsUrl)
          .toPromise()
          .then(response => response.json().data as Tweet[])
          .then(tweets => {
            this.tweetsResult = tweets.filter(tweet => tweet.analysis === quality);
            this.subject.next(this.tweetsResult);
          })
          .catch(this.handleError);
  }


  private handleError(error: any): Promise<any> {
    console.error('An error occurred', error); // for demo purposes only
    return Promise.reject(error.message || error);
  }
}
