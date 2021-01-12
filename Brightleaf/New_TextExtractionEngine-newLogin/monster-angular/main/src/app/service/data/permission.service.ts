import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { environment } from '../../../environments/environment';


export class Permission {
  constructor(
    public permissionId: number,
    public permissionName: string,
    public permissionDescription: string,
    public createdBy: string,
    public creationDate: Date,
    public lastModifiedBy: string,
    public lastModifiedDate: Date

  ) { }
}

@Injectable({
  providedIn: 'root'
})
export class PermissionService {

  constructor(private http: HttpClient) { }

  getAllPermission() {
    return this.http.get<Permission[]>(`${environment.LOGIN_API_URL}/permissions`);
  }

  getPermissionByRoleId(roleId) {
    //console.log("user user "+this.http.get<UserInfo[]>(`${LOGIN_API_URL}/company/${comid}/users`));
    return this.http.get<Permission[]>(`${environment.LOGIN_API_URL}/getPermission/${roleId}`);
  }

  getPermissionByPermissionId(permissionIdString){
    //const permissionId = {"permissionIdString":permissionIdString}
    return this.http.post<Permission[]>(`${environment.LOGIN_API_URL}/permissionList`,permissionIdString);
  }
}
