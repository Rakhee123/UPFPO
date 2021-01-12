import { PipeTransform, Pipe } from "@angular/core";

@Pipe({
    name: 'ignoreResultConsolidatedPipe',
    pure: true
  })
  export class IgnoreResultConsolidatedPipe implements PipeTransform {
    transform(value: string, index2: any,type: any): any {
      return this.setUiData(value,index2,type);
    }

    setUiData(value,index: string,type : string): boolean{
      if(value==undefined){
              return true;    
      }else{
        console.info("---getMemberShipLevel---"+value.ignoreResult);
       return false;
      }
    }
  }