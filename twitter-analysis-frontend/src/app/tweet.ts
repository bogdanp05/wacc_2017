export class Tweet {
  id:string;
  date:string;
  text:string;
  sentimentalAnalysis:string;

  constructor(id:string, date:string, text:string, sentimentalAnalysis:string) {
    this.id = id;
    this.date = date;
    this.text = text;
    this.sentimentalAnalysis = sentimentalAnalysis;
  }

}
