import { Injectable } from '@angular/core';
import { interval, switchMap, startWith, shareReplay } from 'rxjs';
import { FlightEventService } from './flight-event.service';

@Injectable({
  providedIn: 'root'
})
export class EventPollingService {

  constructor(private service: FlightEventService) {}

  // 🔥 SINGLE SOURCE OF TRUTH STREAM
  getLiveEvents() {
    return interval(5000).pipe(
      startWith(0),
      switchMap(() => this.service.getAll()),
      shareReplay({ bufferSize: 1, refCount: true })
    );
  }
}