import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { environment } from '../../../environments/environment';
export class DocumentType {
  constructor(
    public documentTypeId: number,
    public documentDesc: string,
    public documentName: string,
    public createdBy: string
  ) { }
}
@Injectable({
  providedIn: 'root'
})
export class DocumentService {

  constructor(private http: HttpClient) { }

  getAllDocuments() {
    return this.http.get<DocumentType[]>(`${environment.DOC_API_URL}/getDocumentTypeList`);
  }

  createDocumentType(document) {
    return this.http.post(`${environment.DOC_API_URL}/addDocumentType`, document);
  }
  
  UpdateDocumentType(document, DocId) {
    return this.http.put<DocumentType>(`${environment.DOC_API_URL}/updateDocumentType/${DocId}`, document);
  }

  deleteDocumentByDocId(DocId) {
    //console.log("document id "+DocId);
    return this.http.delete(`${environment.DOC_API_URL}/deleteDocumentType/${DocId}`);
  }

  getRuleSetByDocumnetId(documentTypeId) {
    return this.http.get(`${environment.DOC_API_URL}/getRuleSetByDocumentId/${documentTypeId}`);
  }
}
