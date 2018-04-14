import { MessageService } from './../shared/message/message.service';
import { Component, OnInit } from '@angular/core';
import {TranslateService} from '@ngx-translate/core';

@Component({
  selector: 'app-message',
  templateUrl: './message.component.html',
  styleUrls: ['./message.component.css']
})
export class MessageComponent implements OnInit {

  constructor(public messageService: MessageService, translate: TranslateService) { 
    translate.setDefaultLang('en');
    translate.use('en');
  }

  ngOnInit() {
  }

}
