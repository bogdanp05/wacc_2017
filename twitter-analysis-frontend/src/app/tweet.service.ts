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
  private tweetsResult = Array<Tweet>();
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
    this.http.get('http://' + window.location.hostname + ':9000/getTweets/' + 'hola')
          .toPromise()
          .then (function(response){
            console.log(response.json() as Tweet[]);
          });


    return this.http.get('http://' + window.location.hostname + ':9000/getTweets/' + 'hola')
          .toPromise()
          .then(response => response.json() as Tweet[])
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

  filterTweetsWithText(text: string) {

    this.http.get(this.tweetsUrl)
      .toPromise()
      .then(response => response.json().data as Tweet[])
      .then(tweets => {
        this.tweetsResult = tweets.filter(tweet => tweet.content.includes(text));
        this.subject.next(this.tweetsResult);
      })
      .catch(this.handleError);
  }


  private handleError(error: any): Promise<any> {
    console.error('An error occurred', error); // for demo purposes only
    return Promise.reject(error.message || error);
  }
}
