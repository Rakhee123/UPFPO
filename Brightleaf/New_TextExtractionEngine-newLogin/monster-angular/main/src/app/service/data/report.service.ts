import { Injectable } from '@angular/core';
import { environment } from '../../../environments/environment';
import { HttpClient, HttpHeaders } from '@angular/common/http';

@Injectable({
  providedIn: 'root'
})
export class ReportService {

  constructor(private http: HttpClient) { }

  getCompanyWiseExtractionData() {
    return this.http.get<any>(`${environment.MONGO_API_URL}/getCompanyWiseExtractionData`);
  }
  getUserWiseExtractionData() {
    return this.http.get<any>(`${environment.MONGO_API_URL}/getUserWiseExtractionData`);
  }

  exportFileExtractionCompanyWise() {
    let headers = new HttpHeaders();
    headers = headers.append('Accept', 'application/octet-stream');
    //headers = headers.append('Content-Type','application/vnd.openxmlformats-officedocument.spreadsheetml.sheet');
    return this.http.get(`${environment.EXPORT_API_URL}/exportExtractionCompanyWise`, {
      responseType: 'blob',
      headers: headers,
      observe: 'response'
    });
  }

  exportFileExtractionUserWise() {
    let headers = new HttpHeaders();
    headers = headers.append('Accept', 'application/octet-stream');
    //headers = headers.append('Content-Type','application/vnd.openxmlformats-officedocument.spreadsheetml.sheet');
    return this.http.get(`${environment.EXPORT_API_URL}/exportExtractionUserWise`, {
      responseType: 'blob',
      headers: headers,
      observe: 'response'
    });
  }
}
