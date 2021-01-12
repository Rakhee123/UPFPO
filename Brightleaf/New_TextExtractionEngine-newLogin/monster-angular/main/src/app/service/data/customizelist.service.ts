import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { environment } from '../../../environments/environment';

export class CustomizeList {
  constructor(
    public customizeListId: number,
    public attributeId: number,
    public name: string,
    public value:string,
    public defaultValue: boolean,
  ) { }
}


@Injectable({
  providedIn: 'root'
})
export class CustomizelistService {
  
  constructor(private http: HttpClient) { }
  
  getCustomizeListByAttributeId(attrid){
    return this.http.get<CustomizeList[]>(`${environment.DOC_API_URL}/getCustomizeList/${attrid}`);
  }

  createCustomizeAttribute(custom){
    return this.http.post(`${environment.DOC_API_URL}/addCustomizeList`, custom);
  }

  updateCustomizeAttribute(custom){
    return this.http.post<CustomizeList[]>(`${environment.DOC_API_URL}/editCustomizeList`,custom);
  }

  deleteCustomizeAttribute(customAttrid){
     return this.http.delete(`${environment.DOC_API_URL}/deleteCustomizeList/${customAttrid}`);
  }
}
