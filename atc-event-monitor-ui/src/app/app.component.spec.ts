import { TestBed } from '@angular/core/testing';
import { AppComponent } from './app.component';
import { EventPollingService } from './core/services/event-polling.service';
import { of } from 'rxjs';

describe('AppComponent', () => {
  let mockPolling: any;

  beforeEach(async () => {
    mockPolling = {
      getLiveEvents: () => of([
        {
          id: '1',
          flightId: 'EK505',
          severity: 'CRITICAL',
          timestamp: new Date().toISOString()
        }
      ]),
      getLiveStats: () => of({
        LOW: 1,
        MEDIUM: 2,
        HIGH: 3,
        CRITICAL: 4
      })
    };

    await TestBed.configureTestingModule({
      imports: [AppComponent],
      providers: [
        { provide: EventPollingService, useValue: mockPolling }
      ]
    }).compileComponents();
  });

  it('should create app', () => {
    const fixture = TestBed.createComponent(AppComponent);
    const app = fixture.componentInstance;
    expect(app).toBeTruthy();
  });

  it('should load events from service', () => {
    const fixture = TestBed.createComponent(AppComponent);
    fixture.detectChanges();

    const app = fixture.componentInstance;
    expect(app.events.length).toBe(1);
  });

  it('should calculate stats correctly', () => {
    const fixture = TestBed.createComponent(AppComponent);
    fixture.detectChanges();

    const app = fixture.componentInstance;
    expect(app.count('CRITICAL')).toBe(4);
  });

  it('should filter events by severity', () => {
    const fixture = TestBed.createComponent(AppComponent);
    const app = fixture.componentInstance;

    app.events = [
      { id: '1', severity: 'LOW' } as any,
      { id: '2', severity: 'HIGH' } as any
    ];

    app.setSeverityFilter('HIGH');

    expect(app.filteredEvents.length).toBe(1);
  });
});