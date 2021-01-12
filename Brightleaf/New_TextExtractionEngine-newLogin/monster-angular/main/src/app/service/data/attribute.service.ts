import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { environment } from '../../../environments/environment';

export class Attribute {
  constructor(
    public attributeId: number,
    public attributeName: string,
    public attributeDesc: string,
    public qcLevels: number,
    public paragraph: string,
    public attributeType: string,
    public fallbackValue: string,
    public createdBy: string,
    public creationDate: Date,
    public lastModifiedBy: string,
    public lastModificationDate: Date

  ) { }
}

@Injectable({
  providedIn: 'root'
})
export class AttributeService {

  constructor(private http: HttpClient) { }

  getAllAttribute() {
    return this.http.get<Attribute[]>(`${environment.DOC_API_URL}/getAttributeList`);
  }

  createAttributeType(attribute) {
    return this.http.post(`${environment.DOC_API_URL}/addAttribute`, attribute);
  }

  UpdateAttributeType(attribute, AttId) {
    return this.http.put<Attribute>(`${environment.DOC_API_URL}/updateAttribute/${AttId}`, attribute);
  }

  deleteAtrributeByAttId(AttId) {
    return this.http.delete(`${environment.DOC_API_URL}/deleteAttribute/${AttId}`);
  }
}
