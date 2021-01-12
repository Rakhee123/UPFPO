
export let settings = {
    columns: {
      attr_name: {
        title: 'Attribute name',
        filter: true
      },
      name: {
        title: 'Attribute value',
        filter: false
      },
      username: {
        title: 'Status',
        filter: true
      },
      email: {
        title: 'Document name',
        filter: false
      },
      pageno: {
        title: 'Page No.',
        filter: false
      },
      lineno: {
        title: 'Line No.',
        filter: false
      },
    },
    actions:
    {
      add:false,
      edit:false,
      delete:false,
    },
    pager:
    {
      perPage:5
    }
   
  };
  export let data = [
  ];
  