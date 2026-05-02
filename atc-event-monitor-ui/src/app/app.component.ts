import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FlightEventService, FlightEvent } from './core/services/flight-event.service';
import { EventCardComponent } from './components/event-card/event-card.component';

@Component({
  selector: 'app-root',
  standalone: true,
  imports: [CommonModule, EventCardComponent],
  templateUrl: './app.component.html',
})
export class AppComponent implements OnInit {

  events: FlightEvent[] = [];
  filteredEvents: FlightEvent[] = [];

  loading = true;
  error: string | null = null;

  selectedSeverity: 'ALL' | 'LOW' | 'MEDIUM' | 'HIGH' | 'CRITICAL' = 'ALL';

  constructor(private service: FlightEventService) {}

  ngOnInit(): void {
    this.loadEvents();

    // ✅ auto-refresh every 5 sec (huge UX win)
    setInterval(() => this.loadEvents(), 5000);
  }

  loadEvents(): void {
    this.loading = true;

    this.service.getAll().subscribe({
      next: (data: FlightEvent[]) => {
        this.events = [...data].reverse();
        this.applyFilter();
        this.loading = false;
        this.error = null;
      },
      error: () => {
        this.error = 'ATC DATA LINK FAILURE';
        this.loading = false;
      }
    });
  }

  count(severity: string): number {
    return this.events.filter(e => e.severity === severity).length;
  }

  setSeverityFilter(severity: 'ALL' | 'LOW' | 'MEDIUM' | 'HIGH' | 'CRITICAL') {
    this.selectedSeverity = severity;
    this.applyFilter();
  }

  applyFilter() {
    this.filteredEvents =
      this.selectedSeverity === 'ALL'
        ? this.events
        : this.events.filter(e => e.severity === this.selectedSeverity);
  }

  acknowledge(id: string) {
    this.service.updateStatus(id, 'ACKNOWLEDGED').subscribe(() => this.loadEvents());
  }

  resolve(id: string) {
    this.service.updateStatus(id, 'RESOLVED').subscribe(() => this.loadEvents());
  }
}