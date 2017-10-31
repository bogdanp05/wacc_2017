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

    const analysisArray = [0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0];
    for (const tweet of this.tweets) { //Solved that
      const date = new Date(tweet.timestamp);
      const day = date.getDay();
      const hour = date.getHours();

      const firstArrayPosition = (day - 4) * 4;
      if (hour >= 0 && hour <= 6) {
        if (tweet.analysis === 1) {
          analysisArray[firstArrayPosition] += 1;
        }
        else if (tweet.analysis === -1) {
          analysisArray[firstArrayPosition] -= 1;
        }
      }
      else if (hour > 6 && hour <= 12) {
        if (tweet.analysis === 1) {
          analysisArray[firstArrayPosition + 1] += 1;
        }
        else if (tweet.analysis === -1) {
          analysisArray[firstArrayPosition + 1] -= 1;
        }
      }
      else if (hour > 12 && hour <= 18) {
        if (tweet.analysis === 1) {
          analysisArray[firstArrayPosition + 2] += 1;
        }
        else if (tweet.analysis === -1) {
          analysisArray[firstArrayPosition + 2] -= 1;
        }
      }
      else if (hour > 18 && hour <= 23) {
        if (tweet.analysis === 1) {
          analysisArray[firstArrayPosition + 3] += 1;
        }
        else if (tweet.analysis === -1) {
          analysisArray[firstArrayPosition + 3] -= 1;
        }
      }


    }
    /*
    * Diagram
    * 0: 04/../....   1: 6h    2: 12h     3: 18h
    * 4: 05/../....   5: 6h    6: 12h     7: 18h
    * 8: 06/../....   9: 6h    10: 12h   11: 18h
    * */
    return analysisArray;
  }

  private handleError(error: any): Promise<any> {
    console.error('An error occurred', error); // for demo purposes only
    return Promise.reject(error.message || error);
  }
}
