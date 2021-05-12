import { Component, EventEmitter, Input, OnInit, Output } from '@angular/core';

@Component({
  selector: 'app-navbar',
  templateUrl: './navbar.component.html',
  styleUrls: ['./navbar.component.scss']
})
export class NavbarComponent implements OnInit {
  
  @Input() cartToggle: boolean;
  @Output() toggleEvent = new EventEmitter<boolean>();

  constructor() { }

  ngOnInit(): void {
  }

  toggleCart() {
    this.cartToggle = !this.cartToggle;
    this.toggleEvent.emit(this.cartToggle);
  }

}
