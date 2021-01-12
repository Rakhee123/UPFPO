import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { environment } from '../../../environments/environment';

export class Role {
  public getroleId(): number {
    return this.roleId;
  }
  public setroleId(value: number) {
    this.roleId = value;
  }
  constructor(
    public roleId: number,
    public roleName: string,
    public permissionList: string
  ) { }
}

@Injectable({
  providedIn: 'root'
})
export class RoleService {

  constructor(private http: HttpClient) { }

  getAllRole() {
    return this.http.get<Role[]>(`${environment.LOGIN_API_URL}/roles`);
  }
}
