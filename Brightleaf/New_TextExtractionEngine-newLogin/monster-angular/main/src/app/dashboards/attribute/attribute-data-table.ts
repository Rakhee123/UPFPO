import { CustomisedAttributeComponent } from "../customised-attribute/customised-attribute.component";
import { Row } from "ng2-smart-table/lib/data-set/row";
import { DISABLED } from "@angular/forms/src/model";
export let settings = {
  columns: {
    attributeName: {
      title: 'Attribute Name',
      filter: false,
      compareFunction:(direction: any, a: any, b: any) => {
        // Converting strings to lowercase
        let first = typeof a === 'string' ? a.toLowerCase() : a;
        let second = typeof b === 'string' ? b.toLowerCase() : b;

        if (first < second) {
            return -1 * direction;
        }
        if (first > second) {
            return direction;
        }
        return 0;
    }
    },
    attributeDesc: {
      title: 'Description',
      filter: false
    },
    paragraph: {
      title: 'Paragraph',
      defaultValue:'No',
      editor: {
        type: 'list',
        config: {
          list: [
            { value: 'Yes', title: 'Yes' },
            { value: 'No', title: 'No' },
          ],

        },
      },
      filter: false
    },
    attributeType: {
      title: 'Attribute Type',
      editor: {
        type: 'list',
        config: {
          selectText: 'Select',
          list: [
            /*{ value: 'Data Cleaner', title: 'Data Cleaner' },
            { value: 'Tag Appender', title: 'Tag Appender' },*/
            { value : '', title : 'Select'},
            { value: 'Address Finder', title: 'Address Finder' },
            { value: 'Organization Finder', title: 'Organization Finder' },
            { value: 'Location Finder', title: 'Location Finder' },
            { value: 'Find Word', title: 'Find Word' },
            { value: 'Currency Extractor', title: 'Currency Extractor' },
            { value: 'Word to Digit Converter', title: 'Word to Digit Converter' },
            { value: 'Date Extractor', title: 'Date Extractor' },
            /*{ value: 'Word Dependency Identifier', title: 'Word Dependency Identifier' },*/
            { value: 'Percentage Extractor', title: 'Percentage Extractor' },
            /*{ value: 'Duplicate Remover', title: 'Duplicate Remover' },
            { value: 'Chunk Present', title: 'Chunk Present' },*/
            { value: 'Customized Attribute', title: 'Customized Attribute' },
          ],
        },
      },
      filter: false
    },
    fallbackValue: {
      title: 'Fallback Value',
      defaultValue:'N/A',
      filter: false
    },
    customizedAttribute: {
      title: 'Customized Attribute',
      type: 'custom',
      valuePrepareFunction: (cell, row) => row,
      renderComponent: CustomisedAttributeComponent,
      filter: false
    }
  },
  edit: {
    confirmSave: true,
    editButtonContent: '<i class="ti-pencil text-success m-r-10"></i>',
    saveButtonContent: '<i class="ti-save text-success m-r-10"></i>',
    cancelButtonContent: '<i class="ti-close text-danger"></i>'
  },
  add: {
    confirmCreate: true,
    addButtonContent: '<i class="nb-plus">Add New</i>',
    createButtonContent: '<i class="fa fa-plus text-success m-r-10"></i>',
    cancelButtonContent: '<i class="ti-close text-danger"></i>',
  },
  delete: {
    confirmDelete: true,
    deleteButtonContent: '<i class="ti-trash text-danger m-r-20"></i>',
    saveButtonContent: '<i class="ti-save text-success m-r-20"></i>',
    cancelButtonContent: '<i class="ti-close text-danger"></i>'
  },
};
// export let settings2 = {
//   columns: {
//     documentName: {
//       title: 'Document Type',
//       filter: true
//     },
//     documentDesc: {
//       title: 'Document Description',
//       filter: true
//     }
//   },
//   edit: {
//     editButtonContent: '<i class="ti-pencil text-info m-r-10"></i>',
//     saveButtonContent: '<i class="ti-save text-success m-r-10"></i>',
//     cancelButtonContent: '<i class="ti-close text-danger"></i>'
//   },
//   delete: {
//     deleteButtonContent: '<i class="ti-trash text-danger m-r-10"></i>',
//     saveButtonContent: '<i class="ti-save text-success m-r-10"></i>',
//     cancelButtonContent: '<i class="ti-close text-danger"></i>'
//   }
// };
export let attribute = [{

}
];
