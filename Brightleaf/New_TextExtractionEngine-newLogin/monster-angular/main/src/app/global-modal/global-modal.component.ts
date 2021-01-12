import { Component, OnInit } from '@angular/core';
import { Subject } from 'rxjs';
import { BsModalService, BsModalRef } from 'ngx-bootstrap/modal';

@Component({
  selector: 'app-global-modal',
  templateUrl: './global-modal.component.html',
  styleUrls: ['./global-modal.component.css']
})
export class GlobalModalComponent implements OnInit {

  public onClose: Subject<boolean>;
  validationMessages = [];
  title: string;
  btn1: string;
  btn2: string;
  body: string;
  showbtn1: string;
  
  enableBtn: boolean = false;
  enableBtn2: boolean = false;

  showCloseIcon: boolean = true;

  constructor(public bsModalRef: BsModalRef,  private modalService: BsModalService) { }

  ngOnInit() {
    this.onClose = new Subject();
  }

  btnClick1(): void {
    this.onClose.next(false);
    this.bsModalRef.hide();
  }

  btnClick2(): void {
    this.onClose.next(true);
    this.bsModalRef.hide();
  }


}
