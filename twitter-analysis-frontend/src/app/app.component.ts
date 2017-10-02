import { Component } from '@angular/core';
import { TweetService } from './tweet.service';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css'],
  providers: [TweetService]
})
export class AppComponent {
  title = 'app';
  message: string;
  constructor(private tweetService: TweetService) {
  }

  // ngOnInit() {
    // this.tweetService.currentMessage.subscribe(message => this.message = message);
  // }
  search(searchBox): void {
    console.log('hi' + searchBox);
    this.tweetService.changeMessage(searchBox);
  }

}

