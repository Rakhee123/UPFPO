import { Directive, ElementRef, Renderer2 } from '@angular/core';

@Directive({
  selector: '[verifyTrxButton]'
})

export class ResultconsolidatedDirective {

  constructor(private renderer: Renderer2,private elmRef: ElementRef) { }

  showHideVerifyTxnButton(value)
    {
      this.renderer.setStyle(this.elmRef.nativeElement, 'visibility',value);
  }

}
