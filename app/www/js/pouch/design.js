(function () {
  'use strict';

  /**
   * Responsible for creating all the views need to find invoices.
   */
  function createInvoiceDesign() {
    return {
      _id: "_design/_invoice",
      views: {
        key: {
          map: function (doc) {
            if (doc.type === 'invoice') {
              emit(doc.key);
            }
          }.toString()
        },
        coo: {
          map: function (doc) {
            if (doc.type === 'invoice') {
              emit(doc.coo);
            }
          }.toString()
        },
        date: {
          map: function (doc) {
            if (doc.type === 'invoice') {
              emit(doc.date);
            }
          }.toString()
        }
      }
    }
  }

  angular.module('nccv.constants')
    .value('PouchDesign', {
      ALL: [
        createInvoiceDesign()
      ]
    });

})();
