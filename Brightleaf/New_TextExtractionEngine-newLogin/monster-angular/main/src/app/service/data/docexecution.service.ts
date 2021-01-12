import { Injectable } from '@angular/core';
import { HttpClient, HttpEvent, HttpRequest } from '@angular/common/http';
import { environment } from '../../../environments/environment';
import { Observable } from 'rxjs';
@Injectable({
  providedIn: 'root'
})
export class DocexecutionService {
  inputEl: any;

  constructor(private http: HttpClient) { }
  

	docExecutionByFileAndRule(file,userName,company_id,documetTypeId,ruleSetId,transIdentifier) {
  alert("docExecutionByFileAndRule");
        let formData = new FormData();
         for (let i = 0; i < file.length; i++) {
      
                formData.append('file', file.item(i));
                formData.append( 'Content-Type', 'multipart/form-data');
                formData.append('Accept', `application/json`);        
            }
                formData.append('companyId',company_id);
                formData.append('userName',userName);
                formData.append('documetTypeId',documetTypeId);   
                formData.append('ruleSetId',ruleSetId);  
                // formData.append('customRadio',exeFuction);
                formData.append('transIdentifier',transIdentifier);
                return this.http.post<any>(`${environment.EXECUTION_API_URL}/execute`,formData);
          }
}
