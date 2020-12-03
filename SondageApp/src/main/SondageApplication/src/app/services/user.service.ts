import { Injectable } from '@angular/core';
import {HttpClient, HttpHeaders} from '@angular/common/http';
import { Observable } from 'rxjs';
import {SignUpInfo} from '../auth/signup-info';
import {SondageInfo} from './sondage-info';

@Injectable({
  providedIn: 'root'
})
export class UserService {

  public host:string = "http://localhost:8080"

  constructor(private http: HttpClient) { }

  public getRessources(url)
  {
    return this.http.get(this.host + url);
  }

  public createRessources(url,element)
  {
    return this.http.post(this.host+url,element);
  }

  updateRessources(url,element) {
    return this.http.put(this.host+url,element);
  }

  public deleteRessources(url)
  {
    return this.http.delete(this.host+url);
  }

  formattedDate(d = new Date) {
    return [d.getDate(), d.getMonth()+1, d.getFullYear()]
      .map(n => n < 10 ? `0${n}` : `${n}`).join('/');
  }

}
