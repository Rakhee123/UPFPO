import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { BsModalRef, BsModalService } from 'ngx-bootstrap/modal';
import { GlobalModalComponent } from '../../global-modal/global-modal.component';
import { CompanydataService } from '../../service/data/companydata.service';
import { Company } from '../companylist/companylist.component';

@Component({
  selector: 'app-addcompany',
  templateUrl: './addcompany.component.html',
  styleUrls: ['./addcompany.component.css']
})
export class AddcompanyComponent implements OnInit {

  companyId: number
  company: Company
  mfaCheck: any;

  constructor(private companyService: CompanydataService,
    private route: ActivatedRoute,
    private router: Router,
    private bsModalService: BsModalService,
    private bsModalRef: BsModalRef) { }

  ngOnInit() {
    this.companyId = this.route.snapshot.params['comid'];
    this.company = new Company(this.companyId, '', '', '', '', '', 0, 1, 0, '', new Date(), '', new Date(), 0);
    if (this.companyId != -1) {
      this.companyService.getCompanyById(this.companyId).subscribe(
        response => {
          this.company = response;
        }
      )
    }
  }

  saveCompany() {
    this.mfaCheck = this.company.mfa;
    if (this.mfaCheck == true) {
      this.company.mfa = 1;
    }
    else {
      this.company.mfa = 0;
    }
    if (this.companyId == -1) {
      this.company['createdBy'] = sessionStorage.getItem('authenticateUser');
      this.companyService.createCompany(this.company).subscribe(
        response => {
          this.router.navigate(['dashboard/companylist']);
        }
      )
    } else {
      this.company['lastModifiedBy'] = sessionStorage.getItem('authenticateUser');
      this.companyService.editCompany(this.companyId, this.company).subscribe(
        response => {
          this.router.navigate(['dashboard/companylist'])
        })
    }
  }

  companyNameCheck(companyname) {
    if (companyname != null && companyname != "" && companyname.valid) {
      this.companyService.checkCompanyName(companyname).subscribe(
        response => {
          if (response) {
            let initialState = {
              title: 'Information',
              btn2: 'Ok',
              body: `Company name already exists.`,
              enableBtn2: true,
            };

            this.bsModalRef = this.bsModalService.show(GlobalModalComponent, Object.assign({ backdrop: true, ignoreBackdropClick: true, keyboard: false }, { initialState }));
          }
          else {
          }
        }
      )
    }
  }
}
