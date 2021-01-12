import { PipeTransform, Pipe } from "@angular/core";

@Pipe({
    name: 'ignoreResultConsolidateCheckdPipe',
    pure: true
  })
  export class IgnoreResultConsolidatedCheckPipe implements PipeTransform {
    transform(value: string, index2: any,type: any): any {
      return this.setUiData(value,index2,type);
    }

    setUiData(value,index: string,type : string): boolean{
      if(value==undefined){
              return false;    
      }else{
          if(value.ignoreResult=="YES")
          {
              return true;
          }else{
            return false;
          }
      }
    }
  }