import { Component, OnInit } from '@angular/core';

@Component({
  selector: 'app-loading-animation',
  template: ` <div class="lds-dual-ring"></div> `,
  styleUrls: ['./loading-animation.component.scss'],
})
export class LoadingAnimationComponent implements OnInit {
  constructor() {}

  ngOnInit(): void {}
}
