import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { environment } from '../../../environments/environment';

export class UserQc{

  constructor(
    public qcId: number,
    public companyId: number,
    public transactionId: string,
    public qcLevel: number,
    public assignedBy: string,
    public assignedTo: string,
    public status: string,
    public creationDate: Date,
    public lastModifiedBy: string,
    public lastModificationDate: Date

  ) { }
}
@Injectable({
  providedIn: 'root'
})
export class TransactionService {

  constructor(private http: HttpClient) { }

  getAllTransaction(qcId,companyName) {
    return this.http.get<UserQc[]>(`${environment.RESULT_API_URL}/getUserQcList/${qcId}/${companyName}`);
  }
}
