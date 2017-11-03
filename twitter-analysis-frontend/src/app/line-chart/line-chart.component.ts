///<reference path="../../../node_modules/@angular/core/src/metadata/directives.d.ts"/>
import {Component, OnInit} from '@angular/core';
import {TweetService} from '../tweet.service';

@Component({
  selector: 'app-line-chart',
  templateUrl: './line-chart.component.html',
  styleUrls: ['./line-chart.component.css']
})
export class LineChartComponent implements OnInit  {

  constructor(private tweetService:  TweetService) { }
// lineChart
  public lineChartData: Array<any> = [
    {data: [0, 0, 0, 0, 0, 0, 0], label: 'Positives'},
    {data: [0, 0, 0, 0, 0, 0, 0], label: 'Neutral'},
    {data: [0, 0, 0, 0, 0, 0, 0], label: 'Negatives'}
  ];
  public lineChartLabels: Array <any> = ['04/03/2016', '6h', '12h', '18h', '05/03/2016', '6h', '12h', '18h',
    '06/03/2016', '6h', '12h', '18h'];
  public lineChartOptions:any = {
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


  ngOnInit() {
    const amountOfTweetsByTimePositives = this.getAmountOfTweetsByTime(1)
    const amountOfTweetsByTimeNetural = this.getAmountOfTweetsByTime(0)
    const amountOfTweetsByTimeNegatives = this.getAmountOfTweetsByTime(-1)


    this.lineChartData = [
      {data: amountOfTweetsByTimePositives, label: 'Positives'},
      {data: amountOfTweetsByTimeNetural, label: 'Neutral'},
      {data: amountOfTweetsByTimeNegatives, label: 'Negatives'}
    ];
  }

  getAmountOfTweetsByTime(quality: number): Array<number> {
    return this.tweetService.getAmountOfTweetsByTime(quality);
  }

}
