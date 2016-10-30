import{Component}from'@angular/core';
import {$WebSocket}from './angular2-websocket';

@Component({
  selector: 'my-app',
  template: `
   <img src="{{picUrl}}" alt="Photo holder" style="width:300px;">
    <br>
	Json data: {{jsonData}}
	<button type="button" (click)="subscribe($event)">Subscribe to WebSocket</button>
	`
})
export class AppComponent {

  jsonData: string = 'not known';
  picUrl: string = '';
  ws: $Websocket;
  constructor() {
    this.ws = new $WebSocket("ws://localhost:8080/api/ws");
  }

  subscribe($event) {
    console.log("trying to subscribe to ws");
    this.ws = new $WebSocket("ws://localhost:8080/api/ws");
    this.ws.send("Hello");
    this.ws.getDataStream().subscribe(
      res => {
        console.log(res.data)
        var count = res.data;
        console.log('JSON: ' + count);
        this.picUrl = JSON.parse(count).photoUrl;
        this.jsonData = count;
      },
      function(e) { console.log('Error: ' + e.message); },
      function() { console.log('Completed'); }
    );
  }
}

/*
Copyright 2016 Google Inc. All Rights Reserved.
Use of this source code is governed by an MIT-style license that
can be found in the LICENSE file at http://angular.io/license
*/
