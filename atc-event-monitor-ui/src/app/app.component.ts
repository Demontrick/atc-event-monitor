import { Component, OnInit, OnDestroy } from '@angular/core';
import { CommonModule } from '@angular/common';
import { Subscription } from 'rxjs';

import { EventCardComponent } from './components/event-card/event-card.component';
import { EventPollingService } from './core/services/event-polling.service';
import { FlightEvent } from './core/services/flight-event.service';

type Severity = 'LOW' | 'MEDIUM' | 'HIGH' | 'CRITICAL';

interface Stats {
  LOW: number;
  MEDIUM: number;
  HIGH: number;
  CRITICAL: number;
}

@Component({
  selector: 'app-root',
  standalone: true,
  imports: [CommonModule, EventCardComponent],
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css']
})
export class AppComponent implements OnInit, OnDestroy {

  events: FlightEvent[] = [];

  stats: Stats = {
    LOW: 0,
    MEDIUM: 0,
    HIGH: 0,
    CRITICAL: 0
  };

  loading = true;
  error: string | null = null;

  selectedSeverity: 'ALL' | Severity = 'ALL';

  private subs = new Subscription();

  constructor(private polling: EventPollingService) {}

  ngOnInit(): void {

    this.subs.add(
      this.polling.getLiveEvents().subscribe({
        next: (data: FlightEvent[]) => {

          const safe = Array.isArray(data) ? data : [];

          const processed = this.deduplicateAndSort(safe);

          this.events = processed;

          this.stats = this.calculateStats(processed);

          this.loading = false;
        },
        error: () => {
          this.error = 'ATC DATA LINK FAILURE';
          this.loading = false;
        }
      })
    );
  }

  ngOnDestroy(): void {
    this.subs.unsubscribe();
  }

  // 🔥 FILTER
  setSeverityFilter(sev: 'ALL' | Severity): void {
    this.selectedSeverity = sev;
  }

  get filteredEvents(): FlightEvent[] {
    if (this.selectedSeverity === 'ALL') return this.events;
    return this.events.filter(e => e?.severity === this.selectedSeverity);
  }

  // 🔥 STATS
  count(sev: Severity): number {
    return this.stats?.[sev] ?? 0;
  }

  // 🔥 ACTIONS
  acknowledge(id: string): void {
    console.log('ACK:', id);
  }

  resolve(id: string): void {
    console.log('RESOLVE:', id);
  }

  // 🔥 TRACKBY FIX (THIS WAS MISSING — CAUSING YOUR ERROR)
  trackByEventId(index: number, item: FlightEvent): string {
    return item?.id ?? index.toString();
  }

  // 🔥 DEDUPE + SORT
  private deduplicateAndSort(events: FlightEvent[]): FlightEvent[] {

    const map = new Map<string, FlightEvent>();

    for (const e of events) {
      if (e?.id) map.set(e.id, e);
    }

    return Array.from(map.values()).sort((a, b) => {
      return new Date(b.timestamp ?? 0).getTime() -
             new Date(a.timestamp ?? 0).getTime();
    });
  }

  // 🔥 STATS FROM EVENTS
  private calculateStats(events: FlightEvent[]): Stats {

    const result: Stats = {
      LOW: 0,
      MEDIUM: 0,
      HIGH: 0,
      CRITICAL: 0
    };

    for (const e of events) {
      if (e?.severity && e.severity in result) {
        result[e.severity as Severity]++;
      }
    }

    return result;
  }
}