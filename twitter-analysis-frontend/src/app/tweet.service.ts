import { Tweet } from './tweet';
import { TWEETS } from './mock-tweets';
import { Injectable } from '@angular/core';

@Injectable()
export class TweetService {
  getTweets(): Promise<Tweet[]> {
    return Promise.resolve(TWEETS);
  }

  // getHeroesSlowly(): Promise<Hero[]> {
  //   return new Promise(resolve => {
  //     // Simulate server latency with 2 second delay
  //     setTimeout(() => resolve(this.getHeroes()), 2000);
  //   });
  // }

  getTweet(id: number): Promise<Tweet> {
    return this.getTweets()
               .then(tweets => tweets.find(tweet => tweet.id === id));
  }

  filterTweets(quality: number): Promise<Tweet[]>{
    return this.getTweets()
               .then(tweets => tweets.filter(tweet => tweet.analysis=== quality));
  }
}
