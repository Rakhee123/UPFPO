import { Injectable } from '@angular/core';
import { UserInfo } from './userinfo.service';
import { Company } from '../../dashboards/companylist/companylist.component';
import { Role } from './role.service';
import { HttpClient } from '@angular/common/http';
import { environment } from '../../../environments/environment';

export class UserRole {
  public getuserRoleId(): number {
    return this.userRoleId;
  }
  public setuserRoleId(value: number) {
    this.userRoleId = value;
  }
  public getpermissionList(): string {
    return this.permissionList;
  }
  public setpermissionList(value: string) {
    this.permissionList = value;
  }
  
  constructor(
    public userRoleId: number,
    public userInfo: UserInfo,
    public role: Role,
    public company: Company,
    public permissionList: string
  ) { }
}

@Injectable({
  providedIn: 'root'
})
export class UserroleService {
  constructor(private http: HttpClient) { }

  getUserRoleByUsernameAndCompanyId(userName,companyName) {
		return this.http.get<UserRole>(`${environment.LOGIN_API_URL}/getUserName/${userName}/company/${companyName}`);
	}
}
