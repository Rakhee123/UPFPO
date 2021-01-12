import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { map } from 'rxjs/operators';
import { environment } from '../../../environments/environment';
import { Company } from '../../dashboards/companylist/companylist.component';

export const COMPANY_NAME = 'companyName';
export const COMPANY_ID = 'companyId';

@Injectable({
	providedIn: 'root'
})
export class CompanydataService {

	constructor(private http: HttpClient) { }

	getAllCompanyListById() {
		return this.http.get<Company[]>(`${environment.LOGIN_API_URL}/companies`);
	}

	getSetCompanyById(id) {
		let userEmail = sessionStorage.getItem('authenticateUser');
		let sessionId = sessionStorage.getItem('sessionId');
		let companyId = sessionStorage.getItem(COMPANY_ID);

		const content = {
			"email": userEmail,
			"sessionId": sessionId,
			"companyId": companyId
		};
		return this.http.post<Company>(`${environment.LOGIN_API_URL}/setCompanyIdInLoginDetails/${id}`, content).pipe(
			map(
				data => {
					sessionStorage.setItem(COMPANY_NAME, data['companyName']);
					sessionStorage.setItem(COMPANY_ID, data['companyId'].toString());
					return data;
				}
			)
		);
	}

	getCompanyById(id) {
		return this.http.get<Company>(`${environment.LOGIN_API_URL}/company/${id}`);
	}
	getCompanyByIdForCurrentCompanyName(id) {
		return this.http.get<Company>(`${environment.LOGIN_API_URL}/company/${id}`);
	}
	createCompany(company) {
		return this.http.post(`${environment.LOGIN_API_URL}/addCompany`, company);
	}
	editCompany(id, company) {
		return this.http.put<Company>(`${environment.LOGIN_API_URL}/updateCompany/${id}`, company);
	}
	deleteCompany(id) {
		return this.http.delete(`${environment.LOGIN_API_URL}/deleteCompany/${id}`);
	}

	checkCompanyName(companyname) {
		return this.http.get<Company>(`${environment.LOGIN_API_URL}/checkCompanyName/${companyname}`);
	}

	logoutUserBySwitchCompany(contenet) {
		return this.http.post<any>(`${environment.LOGIN_API_URL}/logoutUserBySwitchCompany`, contenet);
	}
}
