import { InMemoryDbService } from 'angular-in-memory-web-api';

/***
* This shoud be used to simulate the calls to the server.
***/

export class InMemoryDataService implements InMemoryDbService {
  createDb() {
    const tweets = [
      { id: 1, date: '2017/09/25 10:14:23' , text: 'mao mao', analysis: 1 },
      { id: 2, date: '2017/09/25 10:14:23' , text: 'mao text', analysis: 0 },
      { id: 3, date: '2017/09/25 10:14:23' , text: 'mtext mao', analysis: 1 },
      { id: 4, date: '2017/09/25 10:14:23' , text: 'mao1 text', analysis: -1 },
      { id: 5, date: '2017/09/25 10:14:23' , text: 'mao1 mao', analysis: 0 },
      { id: 6, date: '2017/09/25 10:14:23' , text: 'mao2 mao', analysis: -1 },
      { id: 7, date: '2017/09/25 10:14:23' , text: 'mao3 mao', analysis: 1 },
      { id: 8, date: '2017/09/25 10:14:23' , text: 'mao4 mao', analysis: 1 },
      { id: 9, date: '2017/09/25 10:14:23' , text: 'mao1 mao', analysis: 1 },
      { id: 10, date: '2017/09/25 10:14:23' , text: 'mao2 mao', analysis: 1 },
      { id: 11, date: '2017/09/25 10:14:23' , text: 'mao4 mao', analysis: 1 }
    ];
    return {tweets};
  }
}
