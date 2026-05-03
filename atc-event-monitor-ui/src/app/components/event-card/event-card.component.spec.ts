import { ComponentFixture, TestBed } from '@angular/core/testing';
import { EventCardComponent } from './event-card.component';
import { vi } from 'vitest';

describe('EventCardComponent', () => {
  let component: EventCardComponent;
  let fixture: ComponentFixture<EventCardComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [EventCardComponent]
    }).compileComponents();

    fixture = TestBed.createComponent(EventCardComponent);
    component = fixture.componentInstance;

    component.event = {
      id: '1',
      flightId: 'EK505',
      severity: 'CRITICAL',
      eventType: 'EMERGENCY_DECLARED',
      altitude: 35000,
      heading: 180,
      timestamp: new Date().toISOString()
    } as any;

    fixture.detectChanges();
  });

  it('should render flight id', () => {
    const compiled = fixture.nativeElement as HTMLElement;
    expect(compiled.textContent).toContain('EK505');
  });

  it('should emit acknowledge event', () => {
    const emitSpy = vi.spyOn(component.acknowledge, 'emit');

    component.acknowledge.emit('1');

    expect(emitSpy).toHaveBeenCalledWith('1');
  });
});