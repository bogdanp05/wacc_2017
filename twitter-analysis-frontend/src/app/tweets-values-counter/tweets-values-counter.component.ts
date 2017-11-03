import { Component, OnInit } from '@angular/core';
import {TweetService} from '../tweet.service';

@Component({
  selector: 'app-tweets-values-counter',
  templateUrl: './tweets-values-counter.component.html',
  styleUrls: ['./tweets-values-counter.component.css']
})
export class TweetsValuesCounterComponent implements OnInit {

  constructor(private tweetService:  TweetService) { }

  ngOnInit() {
    this.updateGraph();
  }

  getNumberOfTweets(quality: number) {
    return this.tweetService.getNumberOfTweets(quality);
  }

  getNumberOfAllTweets() {

    this.updateGraph();
    return this.tweetService.getNumberOfAllTweets();;
  }

  public lineChartData: Array<any> = [
    {data: this.getAmountOfTweetsByTime(1), label: 'Positives'},
    {data: this.getAmountOfTweetsByTime(0), label: 'Neutral'},
    {data: this.getAmountOfTweetsByTime(-1), label: 'Negatives'}
  ];
  public lineChartLabels: Array <any> = ['05/03/2016', '6h', '12h', '18h', '06/03/2016', '6h', '12h', '18h'];
  public lineChartOptions: any = {
    responsive: true
  };
  public lineChartColors: Array<any> = [
    { // Positives
      backgroundColor: 'rgba(255,255,255,0.0)',
      borderColor: 'rgba(24,142,0,1)',
      pointBackgroundColor: 'rgba(24,142,0,1)',
      pointBorderColor: '#fff',
      pointHoverBackgroundColor: '#fff',
      pointHoverBorderColor: 'rgba(148,159,177,0.8)'
    },
    { // Negatives
      backgroundColor: 'rgba(255,255,255,0.0)',
      borderColor: 'rgba(91,91,91,1)',
      pointBackgroundColor: 'rgba(91,91,91,1)',
      pointBorderColor: '#fff',
      pointHoverBackgroundColor: '#fff',
      pointHoverBorderColor: 'rgba(77,83,96,1)'
    },
    { // Neutral
      backgroundColor: 'rgba(255,255,255,0.0)',
      borderColor: 'rgba(185,0,0,1)',
      pointBackgroundColor: 'rgba(185,0,0,1)',
      pointBorderColor: '#fff',
      pointHoverBackgroundColor: '#fff',
      pointHoverBorderColor: 'rgba(148,159,177,0.8)'
    }
  ];
  public lineChartLegend: boolean = true;
  public lineChartType: string = 'line';


  // events
  public chartClicked(e: any): void {
    console.log(e);
  }

  public chartHovered(e: any): void {
    console.log(e);
  }


  getAmountOfTweetsByTime(quality: number): Array<number> {
    return this.tweetService.getAmountOfTweetsByTime(quality);
  }

  updateGraph() {
    const lineChartData = [
      {data: this.getAmountOfTweetsByTime(1), label: 'Positives'},
      {data: this.getAmountOfTweetsByTime(0), label: 'Neutral'},
      {data: this.getAmountOfTweetsByTime(-1), label: 'Negatives'}
    ];

    this.lineChartData = lineChartData;

  }

}
