import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { environment } from '../../../environments/environment';

export const COMPANY_NAME = 'companyName';

@Injectable({
  providedIn: 'root'
})
export class ResultService {

  constructor(private http: HttpClient) { }
  companyName = sessionStorage.getItem(COMPANY_NAME);

  getResultDataDocumentWise(transactionId, qcLevel) {
    return this.http.get<any>(`${environment.MONGO_API_URL}/getMongoData/${this.companyName}/${transactionId}/${qcLevel}`);
  }

  getResultDataConsolidated(transactionId, qcLevel) {
    return this.http.get<any>(`${environment.MONGO_API_URL}/getMongoData/${this.companyName}/${transactionId}/${qcLevel}`);
  }

  exportFileDocWise(qclvl, tranId) {
    let headers = new HttpHeaders();
    headers = headers.append('Accept', 'application/octet-stream');
    return this.http.get(`${environment.EXPORT_API_URL}/exportDocumentWise/${this.companyName}/${tranId}/${qclvl}`, {
      responseType: 'blob',
      headers: headers,
      observe: 'response'
    });
  }

  exportFileConsolWise(qclvl, tranId) {
    let headers = new HttpHeaders();
    headers = headers.append('Accept', 'application/octet-stream');
    return this.http.get(`${environment.EXPORT_API_URL}/exportConsolidated/${this.companyName}/${tranId}/${qclvl}`, {
      responseType: 'blob',
      headers: headers,
      observe: 'response'
    });
  }

  exportFileFullConsolWise(qclvl, tranId) {
    let headers = new HttpHeaders();
    headers = headers.append('Accept', 'application/octet-stream');
    return this.http.get(`${environment.EXPORT_API_URL}/exportConsolidatedFullInformation/${this.companyName}/${tranId}/${qclvl}`, {
      responseType: 'blob',
      headers: headers,
      observe: 'response'
    });
  }

  upadteQCLevel(content, txnId) {
    let companyName = sessionStorage.getItem(COMPANY_NAME);
    return this.http.post<any>(`${environment.MONGO_API_URL}/postQCDataInMongo/${companyName}/${txnId}`, content);
  }

  verifyAttribute(content) {
    return this.http.post<any>(`${environment.MONGO_API_URL}/verifyAttribute`, content);
  }

  verifyAllDoc(txnId, qcLevel, userName) {
    let companyName = sessionStorage.getItem(COMPANY_NAME);
    return this.http.post<any>(`${environment.MONGO_API_URL}/verifyTransaction/${companyName}/${txnId}/${qcLevel}/${userName}`, 'no');
  }

  verifyDocument(content) {
    return this.http.post<any>(`${environment.MONGO_API_URL}/verifyDocument`, content);
  }

  getDocumentFromServer(docName, txn_id) {
    let companyId = sessionStorage.getItem('companyId');
    let headers = new HttpHeaders();
    headers = headers.append('Accept', 'application/octet-stream');

    return this.http.get(`${environment.RESULT_API_URL}/downloadDocument/${docName}/${companyId}/${txn_id}`, {
      headers: headers,
      observe: 'response',
      responseType: 'blob'
    });
  }

  getTransactionVerifyStatus(transactionId, qcLevel) {
    let companyName = sessionStorage.getItem(COMPANY_NAME);
    return this.http.get(`${environment.MONGO_API_URL}/getTransactionVerifyStatus/${transactionId}/${qcLevel}/${companyName}`, { responseType: 'text' });
  }

  addAttribute(txnId, qcLevel, content) {
    let companyName = sessionStorage.getItem(COMPANY_NAME);
    return this.http.post<any>(`${environment.MONGO_API_URL}/postAttributeInMongo/${companyName}/${txnId}/${qcLevel}`, content);
  }

  ignoreResult(content) {
    let companyName = sessionStorage.getItem(COMPANY_NAME);
    return this.http.post<any>(`${environment.MONGO_API_URL}/ignoreResult/${companyName}`, content);
  }

  updateAssingedUser(userName, transactionId, qcLevel) {
    let content = {
      'userName': userName,
      'transactionId': transactionId,
      'qcLevel': qcLevel
    };
    return this.http.post(`${environment.RESULT_API_URL}/updateAssingedUser`, content, { responseType: 'text' });
  }

  addValue(content) {
    let companyName = sessionStorage.getItem(COMPANY_NAME);
    return this.http.post<any>(`${environment.MONGO_API_URL}/addValue/${companyName}`, content);
  }


  deleteSingleTransaction(content) {
    return this.http.request('delete', `${environment.MONGO_API_URL}/deleteTransaction`, { body: content });
  }

  deleteTransactionForCompany(content) {
    return this.http.request('delete', `${environment.MONGO_API_URL}/deleteTransactionsCompanywise`, { body: content });
  }

  deleteTransactionsDatewise(content) {
    return this.http.request('delete', `${environment.MONGO_API_URL}/deleteTransactionsDatewise`, { body: content });
  }

  changeCustemValue(content,txnId)
  {
    let companyName = sessionStorage.getItem(COMPANY_NAME);
    return this.http.post<any>(`${environment.MONGO_API_URL}/changeCustemValue/${companyName}/${txnId}`, content);

  }

  getRuleById(ruleId) {
    return this.http.get(`${environment.RULE_API_URL}/getRuleUsingRuleId/${ruleId}`);
  }

  getAttributeByName(attributeName){
    return this.http.get(`${environment.DOC_API_URL}/getAttributeByName/${attributeName}`);
  }

  getUserQcByTxnIdAndQcLevel(txnId,qclevel)
  {
    return this.http.get(`${environment.RESULT_API_URL}/getUserQcByTxnIdAndQcLevel/${qclevel}/${txnId}`);
  }
}
