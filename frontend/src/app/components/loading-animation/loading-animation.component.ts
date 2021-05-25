import { Component, OnInit } from '@angular/core';

@Component({
  selector: 'app-loading-animation',
  template: ` <div class="lds-ellipsis">
    <div></div>
    <div></div>
    <div></div>
    <div></div>
  </div>`,
  styleUrls: ['./loading-animation.component.scss'],
})
export class LoadingAnimationComponent implements OnInit {
  constructor() {}

  ngOnInit(): void {}
}
