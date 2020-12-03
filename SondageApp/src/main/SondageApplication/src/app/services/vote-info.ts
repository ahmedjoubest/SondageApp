import {DateInfo} from './date-info';
import {LieuInfo} from './lieu-info';

export class VoteInfo {
    id : number;
    sondage: number;
    user: string;
    date: string;
    lieu: string;


  constructor(sondage: number, user: string, date: string, lieu: string) {
    this.sondage = sondage;
    this.user = user;
    this.date = date;
    this.lieu = lieu;
  }
}
