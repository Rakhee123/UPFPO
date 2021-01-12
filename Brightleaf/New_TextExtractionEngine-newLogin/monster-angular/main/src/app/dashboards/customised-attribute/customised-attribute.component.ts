import { Component, OnInit } from '@angular/core';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';
import * as tableData from './customise-data-table';
import { LocalDataSource } from 'ng2-smart-table';
import { BsModalRef, BsModalService } from 'ngx-bootstrap/modal';
import { GlobalModalComponent } from '../../global-modal/global-modal.component';
import { CustomizeList, CustomizelistService } from '../../service/data/customizelist.service';

@Component({
  selector: 'app-customised-attribute',
  templateUrl: './customised-attribute.component.html',
  styleUrls: ['./customised-attribute.component.css']
})
export class CustomisedAttributeComponent implements OnInit {

  customs: CustomizeList[];
  customSource: LocalDataSource;
  settings = tableData.setting;
  public value;
  selectedAttributeId: any;
  paging = [];
  disableCustomizeButton: boolean = false;
  defaultValues = [];
  flag: boolean = false;
  singleDefault: boolean;
  updatedAttribute = [];
  updateDefault: boolean;
  createdAttribute = [];
  createDefault: boolean;

  constructor(private modalService: NgbModal,
    private bsModalRef: BsModalRef,
    private bsModalService: BsModalService,
    private customizelistservice: CustomizelistService) { }

  ngOnInit() {
    this.selectedAttributeId = this.value['attributeId'];
    if (this.value['attributeType'] == "Customized Attribute") {
      this.disableCustomizeButton = true;
    }
  }

  getCustomizelist() {
    this.customizelistservice.getCustomizeListByAttributeId(this.selectedAttributeId).subscribe(
      response => {
        this.customs = response['CustomizeList'];
        this.customSource = new LocalDataSource(this.customs);
        this.customSource['pagingConf'] = { page: 1, perPage: 5 };
      })
  }

  openCustomAttribute(customAttribute: NgbModal) {
    this.getCustomizelist();
    this.modalService.open(customAttribute, Object.assign({backdrop: false,ignoreBackdropClick: true,keyboard:false, size : 'lg'}));
  }

  onCreateConfirm(event, activeattrId) {
     if(this.checkValidation(event)==false){
      return false;
     }
    for(let i=0;i<this.customs.length;i++){
      if(event.newData.name.toLowerCase() == this.customs[i].name.toLowerCase()){

        let initialState = {
          title: 'Customized Attribute',
          btn2: 'Ok',
          body: 'Custom name already exist.',
          enableBtn2: true
        };
        this.bsModalRef = this.bsModalService.show(GlobalModalComponent, Object.assign({ backdrop: true, ignoreBackdropClick: true, keyboard: false }, { initialState }));
        return false;
      }
    }
    var someArray = this.customSource['data'];
    someArray.forEach((item, index) => {
      if (item.defaultValue == true) {
        this.flag = true;
      }
    });

    if (this.flag == true && event.newData.defaultValue == "true") {
      let initialState = {
        title: 'Customized Attribute',
        btn2: 'Ok',
        body: 'You can only choose one customized attribute as default value.',
        enableBtn2: true
      };
      this.bsModalRef = this.bsModalService.show(GlobalModalComponent, Object.assign({ backdrop: true, ignoreBackdropClick: true, keyboard: false }, { initialState }));
    }
    else {
      this.flag = false;
      event.newData.attributeId = activeattrId;
      event.newData['createdBy'] = sessionStorage.getItem('authenticateUser');

      this.customizelistservice.createCustomizeAttribute(event.newData).subscribe(
        response => {
          event.confirm.resolve(event.newData);
          this.createdAttribute = event.newData;
          console.log(this.createdAttribute)
          this.getCustomizelist();
        })
    }
  }

  checkValidation(e){
  if(e.newData.value=="" && e.newData.defaultValue=="" && e.newData.name=="")
      {     
        let initialState = {
                      title: 'Customized Attribute',
                      btn2: 'Ok',
                      body: 'Empty row can not be inserted.',
                      enableBtn2: true
                    };
              this.bsModalRef = this.bsModalService.show(GlobalModalComponent, Object.assign({ backdrop: true, ignoreBackdropClick: true, keyboard: false }, { initialState }));
              return false;
      }

  if(e.newData.name=="" ){
        let initialState = {
          title: 'Customized Attribute',
          btn2: 'Ok',
          body: 'Please enter custom name.',
          enableBtn2: true
        };
        this.bsModalRef = this.bsModalService.show(GlobalModalComponent, Object.assign({ backdrop: true, ignoreBackdropClick: true, keyboard: false }, { initialState }));
        return false;
  }

  if(e.newData.value=="" ){
                let initialState = {
                  title: 'Customized Attribute',
                  btn2: 'Ok',
                  body: 'Please enter custom value.',
                  enableBtn2: true
                };
                this.bsModalRef = this.bsModalService.show(GlobalModalComponent, Object.assign({ backdrop: true, ignoreBackdropClick: true, keyboard: false }, { initialState }));
        return false;
  }

  if(e.newData.defaultValue==="" ){
        let initialState = {
          title: 'Customized Attribute',
          btn2: 'Ok',
          body: 'Please select custom  default value.',
          enableBtn2: true
        };
        this.bsModalRef = this.bsModalService.show(GlobalModalComponent, Object.assign({ backdrop: true, ignoreBackdropClick: true, keyboard: false }, { initialState }));
        return false;
  }

  return true;
  }

  onDeleteConfirm(event) {
    let initialState = {
      title: 'Delete Customized Attribute',
      btn1: 'No',
      btn2: 'Yes',
      body: 'Are you sure you want to delete this customized attribute?',
      enableBtn: true,
      enableBtn2: true,
    };

    this.bsModalRef = this.bsModalService.show(GlobalModalComponent, Object.assign({ backdrop: true, ignoreBackdropClick: true, keyboard: false }, { initialState }));
    this.bsModalRef.content.onClose.subscribe(result => {
      if (result) {
        event.confirm.resolve();
        this.customizelistservice.deleteCustomizeAttribute(event.data.customizeListId).subscribe(
          response => {
            let initialState = {
              title: 'Delete Customized Attribute',
              btn2: 'Ok',
              body: 'Customized attribute deleted successfully.',
              enableBtn2: true
            };

            this.bsModalRef = this.bsModalService.show(GlobalModalComponent, Object.assign({ backdrop: true, ignoreBackdropClick: true, keyboard: false }, { initialState }));

          })
      }
      else {
        event.confirm.reject();
      }
    })
  }

  onSaveConfirm(event) {
    if(this.checkValidation(event)==false){
      return false;
     }

     for(let i=0;i<this.customs.length;i++){
      if(event.newData.name.toLowerCase() == this.customs[i].name.toLowerCase()){

        let initialState = {
          title: 'Customized Attribute',
          btn2: 'Ok',
          body: 'Custom name already exist.',
          enableBtn2: true
        };
        this.bsModalRef = this.bsModalService.show(GlobalModalComponent, Object.assign({ backdrop: true, ignoreBackdropClick: true, keyboard: false }, { initialState }));
        return false;
      }
    }

    var someArray = this.customSource['data'];
    someArray.forEach((item, index) => {
      if (item.defaultValue == true) {
        this.flag = true;
      }
    });

    if (this.flag == true && event.newData.defaultValue == "true") {
      let initialState = {
        title: 'Customized Attribute',
        btn2: 'Ok',
        body: 'You can only choose one customized attribute as default value.',
        enableBtn2: true
      };
      this.bsModalRef = this.bsModalService.show(GlobalModalComponent, Object.assign({ backdrop: true, ignoreBackdropClick: true, keyboard: false }, { initialState }));
    }
    else {
      this.flag = false;
      event.newData['lastModifiedBy'] = sessionStorage.getItem('authenticateUser');
      this.customizelistservice.updateCustomizeAttribute(event.newData).subscribe(
        response => {
          event.confirm.resolve(event.newData);
          this.updatedAttribute = event.newData;
        })
    }
  }

  customizeSubmit() {
    for (let i = 0; i < this.customSource['data'].length; i++) {
      if (this.customSource['data'][i]['defaultValue'] == true || this.customSource['data'][i]['defaultValue'] == "true") {
        this.singleDefault = true;
      }
    }

    if (this.createdAttribute['defaultValue'] == "false" && this.singleDefault == undefined) {
      this.createDefault = true;
    }

    if (this.updatedAttribute['defaultValue'] == "false" && this.singleDefault == undefined) {
      this.updateDefault = true;
    }
   
    if (this.singleDefault == false || this.singleDefault == undefined || this.updateDefault == true || this.createDefault == true) {
      let initialState = {
        title: 'Customized Attribute',
        btn2: 'Ok',
        body: 'You have to choose one customized attribute as default value.',
        enableBtn2: true
      };
      this.bsModalRef = this.bsModalService.show(GlobalModalComponent, Object.assign({ backdrop: true, ignoreBackdropClick: true, keyboard: false }, { initialState }));
    }
    else {
      this.modalService.dismissAll();
    }
    this.singleDefault = false;
    this.updateDefault = false;
    this.createDefault = false;
  }
}
