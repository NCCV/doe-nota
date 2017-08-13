(function () {
  'use strict';

  /**
   * Responsible for managing invoices.
   */
  function service(pouch) {
    var self = this;

    /**
     * Public functions.
     */
    self.put = put;
    self.findAll = findAll;

    /**
     * If document has ID, update the current document.
     * If not, associate a new ID and save the document.
     *
     * @param document Document to be saved
     * @returns {Promise} Promise to be resolved later
     */
    function put(document) {
      document.type = 'invoice';
      return pouch.save(document);
    }

    /**
     * Find all invoices in the database. Options are applied
     * just if provided.
     *
     * @param view Name of the view to search
     * @param options Options to the query
     * @return {Promise} Promise to be resolved later
     */
    function findAll(view, options) {
      var queryOptions = {
        descending: options && options.order === 'desc',
        include_docs: true
      };

      function transform(result) {
        return result.rows.map(function (row) {
          return row.doc;
        });
      }

      return pouch
        .findAll('_invoice/' + view, queryOptions)
        .then(transform);
    }

    return self;
  }

  service.$inject = ['pouch'];
  angular.module('nccv.services').service('invoice.Service', service);

})();
