import { Component, Input, Output, EventEmitter } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FlightEvent } from '../../core/services/flight-event.service';

@Component({
  selector: 'app-event-card',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './event-card.component.html',
})
export class EventCardComponent {

  @Input() event!: FlightEvent;

  @Output() ack = new EventEmitter<string>();
  @Output() resolve = new EventEmitter<string>();

  onAck() {
    this.ack.emit(this.event.id);
  }

  onResolve() {
    this.resolve.emit(this.event.id);
  }
}