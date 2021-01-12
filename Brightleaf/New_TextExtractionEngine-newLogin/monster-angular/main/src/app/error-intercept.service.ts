import { Injectable } from '@angular/core';
import { Observable, ReplaySubject } from 'rxjs';
import { Subject } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class ErrorInterceptService {
  
  constructor() { }

  notify: ReplaySubject<any> = new ReplaySubject();

  onNotify(): Observable<any> {
      return this.notify.asObservable();
  }
}
