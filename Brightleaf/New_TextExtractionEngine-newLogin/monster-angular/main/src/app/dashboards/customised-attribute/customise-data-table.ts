import { CustomisedAttributeComponent } from "../customised-attribute/customised-attribute.component";

export let setting = {
    columns: {
      name: {
        title: 'Custom Name',
        filter: false
      },
      value: {
        title: 'Custom Value',
        filter: false
      },
      defaultValue : {
        title : 'Default Value',
        editor: {
          type: 'list',
          config: {
            selectText: 'Select',
            list: [
              { value: true, title: 'Yes' },
              { value: false, title: 'No' },
            ],
          },
        },
        filter: false
    }
  },
    edit: {
      confirmSave: true,
      editButtonContent: '<i class="ti-pencil text-success m-r-10"></i>',
      saveButtonContent: '<i class="ti-save text-success m-r-10"></i>',
      cancelButtonContent: '<i class="ti-close text-danger"></i>'
    },
    delete: {
      confirmDelete: true,
      deleteButtonContent: '<i class="ti-trash text-danger m-r-10"></i>'
    },add: {
      confirmCreate: true,
      addButtonContent: '<i class="nb-plus">Add New</i>',
      createButtonContent: '<i class="fa fa-plus text-success m-r-10"></i>',
      cancelButtonContent: '<i class="ti-close text-danger"></i>',
    }
};

export let custom = [{
 
}
];