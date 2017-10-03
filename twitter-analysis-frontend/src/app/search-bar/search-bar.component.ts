import { Component, OnInit } from '@angular/core';
import {TweetService} from '../tweet.service';

@Component({
  selector: 'app-search-bar',
  templateUrl: './search-bar.component.html',
  styleUrls: ['./search-bar.component.css']
})
export class SearchBarComponent implements OnInit {

  searchValue = '';
  constructor(private tweetService:  TweetService) { }

  ngOnInit() {
  }


  search(searchBox): void {
    this.tweetService.filterTweetsWithText(searchBox);
  }

}
