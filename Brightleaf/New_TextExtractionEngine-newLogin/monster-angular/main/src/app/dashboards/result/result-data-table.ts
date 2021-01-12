export let settings = {
  columns: {
    transactionId: {
      type: 'html',
      title: 'Transaction Id',
      filter: true
    },
    creationDate: {
      title: 'Transaction Date',
      filter: true
    },
    status: {
      title: 'Status',
      filter: true
    },
    assignedBy: {
      title: 'Assigned From',
      filter: true
    },
    assignedTo: {
      title: 'Assigned To',
      filter: true
    },

  },
  actions:
  {
    add: false,
    edit: false,
    delete: false,
  },
  pager:
  {
    perPage: 5
  }

};

export let data = [
];

