export let settings = {
    columns: {
      id: {
        title: 'Attribute name',
        filter: true
      },
      name: {
        title: 'Attribute value',
        filter: false,
        type: 'html',
        editable:true,
      },
      username: {
        title: 'Status',
        filter: false,
        type: 'html'
      },
      email: {
        title: 'Document name',
        filter: false,
        type: 'html'
      },
      pageno: {
        title: 'Page No.',
        filter: false,
        type: 'html'
      }//,
      //lineno: {
      //  title: 'Line No.',
      //  filter: false,
      //  type: 'html'
      //}
    },
    edit: {
      editButtonContent: '<i class="ti-pencil text-info m-r-10"></i>',
      saveButtonContent: '<i class="ti-save text-success m-r-10"></i>',
      cancelButtonContent: '<i class="ti-close text-danger"></i>'
    },
    delete: {
      deleteButtonContent: '<i class="ti-trash text-danger m-r-10"></i>',
      saveButtonContent: '<i class="ti-save text-success m-r-10"></i>',
      cancelButtonContent: '<i class="ti-close text-danger"></i>'
    },
     pager:
    {
      perPage: 8
    },actions: {
      add: false,
      }
  };
  
  export let data = [
    {
      id: 1,
      type:'button',
      name: '<button>view</button>',
      username: '<i class="ti-close text-danger"></i>',
      email: '<i class="ti-close text-danger"></i>'
    },
    {
      id: 1,
      name: 'Leanne Graham',
      username: 'verified',
      email: 'Sincere@april.biz'
    },
    {
      id: 2,
      name: 'Ervin Howell',
      username: 'not-verified',
      email: 'Shanna@melissa.tv'
    },
    {
      id: 3,
      name: 'Ervin Howell',
      username: 'not-verified',
      email: 'Shanna@melissa.tv'
    },
    {
      id: 4,
      name: 'Ervin Howell',
      username: 'not-verified',
      email: 'Shanna@melissa.tv'
    },
    {
      id: 5,
      name: 'Ervin Howell',
      username: 'not-verified',
      email: 'Shanna@melissa.tv'
    },
    {
      id: 6,
      name: 'Ervin Howell',
      username: 'not-verified',
      email: 'Shanna@melissa.tv'
    },
    {
      id: 7,
      name: 'Ervin Howell',
      username: 'not-verified',
      email: 'Shanna@melissa.tv'
    },
    {
      id: 8,
      name: 'Ervin Howell',
      username: 'not-verified',
      email: 'Shanna@melissa.tv'
    },
    {
      id: 9,
      name: 'Ervin Howell',
      username: 'not-verified',
      email: 'Shanna@melissa.tv'
    },
    {
      id: 10,
      name: 'Ervin Howell',
      username: 'not-verified',
      email: 'Shanna@melissa.tv'
    },
    {
      id: 11,
      name: 'Ervin Howell',
      username: 'not-verified',
      email: 'Shanna@melissa.tv'
    },
    {
      id: 12,
      name: 'Ervin Howell',
      username: 'not-verified',
      email: 'Shanna@melissa.tv'
    }
    
  ];
  