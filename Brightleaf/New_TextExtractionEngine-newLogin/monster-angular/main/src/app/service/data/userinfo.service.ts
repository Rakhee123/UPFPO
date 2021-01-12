import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { map } from 'rxjs/operators';
import { environment } from '../../../environments/environment';

export const COMPANY_NAME = 'companyName';
export class UserInfo {

	public getuserName(): string {
		return this.userName;
	}
	public setuserName(value: string) {
		this.userName = value;
	}

	public getuserAddress(): string {
		return this.userAddress;
	}
	public setuserAddress(value: string) {
		this.userAddress = value;
	}
	public getlastName(): string {
		return this.lastName;
	}
	public setlastName(value: string) {
		this.lastName = value;
	}

	public getmiddleName(): string {
		return this.middleName;
	}
	public setmiddleName(value: string) {
		this.middleName = value;
	}
	public getfirstName(): string {
		return this.firstName;
	}
	public setfirstName(value: string) {
		this.firstName = value;
	}
	public getuserEmail(): string {
		return this.userEmail;
	}
	public setuserEmail(value: string) {
		this.userEmail = value;
	}

	public getuserId(): number {
		return this.userId;
	}
	public setuserId(value: number) {
		this.userId = value;
	}
	getUserPassword(): string {
		return this.userPassword;
	}

	setUserPassword(value: string) {
		this.userPassword = value
	}
	getGender(): string {
		return this.gender;
	}
	setGender(value: string) {
		this.gender = value
	}
	setUserTelephone(value: string) {
		this.userTelephone = value
	}
	getUserTelephone(): string {
		return this.userTelephone;
	}
	setCreatedBy(value: string) {
		this.createdBy = value
	}
	getCreatedBy(): string {
		return this.createdBy;
	}
	setLastModifiedBy(value: string) {
		this.lastModifiedBy = value
	}
	getlLastModifiedBy(): string {
		return this.lastModifiedBy;
	}


	constructor(
		public userId: number,
		public userEmail: string,
		public userPassword: string,
		public userName: string,
		public firstName: string,
		public middleName: string,
		public lastName: string,
		public gender: string,
		public userTelephone: string,
		public userAddress: string,
		public createdBy: string,
		public creationDate: Date,
		public lastModifiedBy: string,
		public lastModifiedDate: Date

	) { }
}

@Injectable({
	providedIn: 'root'
})
export class UserinfoService {

	constructor(private http: HttpClient) { }

	emailCheckByEmailAndComId(username, comid) {
		return this.http.get<UserInfo>(`${environment.LOGIN_API_URL}/company/${comid}/checkMailId/${username}`);
	}
	getUserInfoByUserName(username) {
		return this.http.get<UserInfo>(`${environment.LOGIN_API_URL}/user/${username}`);
	}

	getUserInfoByUserId(comId, uId) {
		return this.http.get<UserInfo>(`${environment.LOGIN_API_URL}/company/${comId}/user/${uId}`);
	}

	getUserCompanyInfoByUserName(username) {
		return this.http.get<UserInfo>(`${environment.LOGIN_API_URL}/userCompany/${username}`).pipe(
			map(
				data => {
					return data;
				}
			)
		);
	}

	getSessionCompanyName() {
		return sessionStorage.getItem(COMPANY_NAME);
	}

	getUserListByCompanyId(comid) {
		return this.http.get<UserInfo[]>(`${environment.LOGIN_API_URL}/company/${comid}/users`);
	}
	createUser(user, comid) {
		return this.http.post(`${environment.LOGIN_API_URL}/${comid}/createUser`, user);
	}
	editUser(uId, comId, user) {
		return this.http.put<UserInfo>(`${environment.LOGIN_API_URL}/company/${comId}/edituser/${uId}`, user);
	}
	deleteUserByCompanyIdAndUserId(comId, uId) {
		return this.http.delete(`${environment.LOGIN_API_URL}/company/${comId}/deleteUser/${uId}`);
	}

	generateOtp(username) {
		return this.http.get<string>(`${environment.LOGIN_API_URL}/genrateOTP/${username}`);
	}

	validateOtp(otpId, otp) {
		return this.http.get<any>(`${environment.LOGIN_API_URL}/validateOtp/${otpId}/${otp}`);
	}

	forgotPasswordSendEmail(email) {
		return this.http.post(`${environment.LOGIN_API_URL}/sendEmailLinkPassword`, email, { responseType: 'text' });
	}

	createPasswordSendEmail(newPassword) {
		return this.http.post(`${environment.LOGIN_API_URL}/sendEmailLinkForNewPassword`, newPassword);
	}

	changePasswordForUser(resetpassword) {
		return this.http.post(`${environment.LOGIN_API_URL}/changeForgotPassword`, resetpassword);
	}
	checkCurrentPasswordCorrect(checkPassword) {
		return this.http.post(`${environment.LOGIN_API_URL}/checkPassword`, checkPassword);
	}
	updateUserProfile(user, username) {
		return this.http.put<any>(`${environment.LOGIN_API_URL}/editProfile/${username}`, user);
	}
}
