import { Component, OnInit, ViewContainerRef, ViewChild } from '@angular/core';
import { ReportService } from '../../service/data/report.service';
import { NgbTabset } from '@ng-bootstrap/ng-bootstrap';
import * as fileSaver from 'file-saver'; // npm i --save file-saver

@Component({
  selector: 'app-report',
  templateUrl: './report.component.html',
  styleUrls: ['./report.component.css']
})
export class ReportComponent implements OnInit {
  currentJustify = 'start';
  currentOrientation = 'horizontal';
  UserReport = [];
  CompanyReport = [];
  constructor(private reportService: ReportService) { }

  ngOnInit() {
    this.fetchCompanyWiseExtraction(1);
  }
  fetchCompanyWiseExtraction(id) {
    if (id == 1) {
      this.reportService.getCompanyWiseExtractionData().subscribe(
        response => {
          this.CompanyReport = response;
          $(function () {
            $('#myTable').DataTable({
              "paging": true,
              "searching": true,
              "pageLength": 5,
              "lengthChange": false,
              "order": [],   /* Disable initial sort */
              "dom": '<"pull-left"f><"pull-right"l>tip'
            });
            $("#myTable_paginate").css("font-size","14px");
            $("#myTable_info").css("font-size","14px");
          });
        })
    }
    if (id == 2) {
      this.reportService.getUserWiseExtractionData().subscribe(
        response => {
          this.UserReport = response;
          $(function () {
            $('#myTable').DataTable({
              "paging": true,
              "searching": true,
              "pageLength": 5,
              "lengthChange": false,
              "order": [],   /* Disable initial sort */
              "dom": '<"pull-left"f><"pull-right"l>tip'
            });
            $("#myTable_paginate").css("font-size","14px");
            $("#myTable_info").css("font-size","14px");
          });
        })
    }
  }
  ExportCompanyWise() {
    this.reportService.exportFileExtractionCompanyWise().subscribe(
      response => {
        this.saveFile(response.body, response.headers.get("Content-Disposition"));
      }
    )
  }

  ExportUserWise() {
    this.reportService.exportFileExtractionUserWise().subscribe(
      response => {
        this.saveFile(response.body, response.headers.get("Content-Disposition"));
      }
    )
  }
  saveFile(data: any, filename?: string) {
    const blob = new Blob([data], { type: 'application/octet-stream' });
    fileSaver.saveAs(blob, filename);
  }
  private tabSet: ViewContainerRef;
  @ViewChild(NgbTabset) set content(content: ViewContainerRef) {
    this.tabSet = content;
  };

  fetchNews(event: any) {
    this.fetchCompanyWiseExtraction(event.nextId);
  }

}
