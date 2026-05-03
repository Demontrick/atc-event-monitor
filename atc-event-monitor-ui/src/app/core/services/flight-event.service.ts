import { Injectable, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

export interface FlightEvent {
  id: string;
  eventId: string;
  flightId: string;
  aircraftType: string;
  altitude: number;
  heading: number;
  eventType: string;
  severity: 'LOW' | 'MEDIUM' | 'HIGH' | 'CRITICAL';
  timestamp: string;
  location: string;
  status: 'ACTIVE' | 'ACKNOWLEDGED' | 'RESOLVED';
}

@Injectable({
  providedIn: 'root'
})
export class FlightEventService {

  private http = inject(HttpClient);

  // ✅ FIX: full API path (NOT just base domain)
  private baseUrl = 'https://redesigned-space-rotary-phone-vj46w597qjjfw747-8080.app.github.dev/api/flights/events';

  getAll(): Observable<FlightEvent[]> {
    return this.http.get<FlightEvent[]>(this.baseUrl);
  }

  updateStatus(id: string, status: string): Observable<FlightEvent> {
    return this.http.patch<FlightEvent>(
      `${this.baseUrl}/${id}/status?status=${status}`, {}
    );
  }
  getStats() {
  return this.http.get<any>(this.baseUrl + '/stats');
}
}