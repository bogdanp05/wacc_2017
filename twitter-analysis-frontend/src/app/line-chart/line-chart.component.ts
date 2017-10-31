///<reference path="../../../node_modules/@angular/core/src/metadata/directives.d.ts"/>
import {Component, OnInit} from '@angular/core';
import {TweetService} from "../tweet.service";

@Component({
  selector: 'app-line-chart',
  templateUrl: './line-chart.component.html',
  styleUrls: ['./line-chart.component.css']
})
export class LineChartComponent implements OnInit  {

  constructor(private tweetService:  TweetService) { }

  public lineChartData: Array <any> = [
    {data: [18, -1, 48, -77, 9, 100, 27, -40, 0, 0, 0, 1], label: 'Tweets'}
  ];
  public lineChartLabels: Array <any> = ['04/03/2016', '6h', '12h', '18h', '05/03/2016', '6h', '12h', '18h',
    '06/03/2016', '6h', '12h', '18h'];
  public lineChartOptions: any = {
    responsive: true
  };
  public lineChartColors: Array <any> = [
    { // grey
      backgroundColor: 'rgba(148,159,177,0.2)',
      borderColor: 'rgba(148,159,177,1)',
      pointBackgroundColor: 'rgba(148,159,177,1)',
      pointBorderColor: '#fff',
      pointHoverBackgroundColor: '#fff',
      pointHoverBorderColor: 'rgba(148,159,177,0.8)'
    }
  ];
  public lineChartLegend = true;
  public lineChartType = 'line';

  ngOnInit() {
    const amountOfTweetsByMonth = this.getAmountOfTweetsByMonth();
    this.lineChartData = [
      {data:  amountOfTweetsByMonth, label: 'Tweets'}
    ];
  }

  getAmountOfTweetsByMonth(): Array<number> {
    return this.tweetService.getAmountOfTweetsByMonth();
  }

  // events
  public chartClicked(e: any): void {
    console.log(e);
  }

  public chartHovered(e: any): void {
    console.log(e);
  }
}
