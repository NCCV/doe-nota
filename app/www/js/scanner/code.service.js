(function () {
  'use strict';

  /**
   * Responsible for processing ocr based data.
   */
  function service($q, $moment, pouch, invoiceService) {
    var self = this;

    /**
     * Public functions.
     */
    self.process = process;

    /**
     * Initialize this service.
     *
     * @return {Object} This service
     */
    function initialize() {
      return self;
    }

    /**
     * Process the data from a specific event, showing toast
     * when an error occurs.
     *
     * @param event Event information
     * @param data Event data from scanner
     */
    function process(event, data) {
      var q = $q.defer();
      var document = undefined;

      try {
        document = extractDocument(data);
        document.status = 'scanned';
      } catch (ex) {
        return $q.reject(ex.forbidden);
      }

      pouch
        .findAll('_invoice/key', {
          key: document.key, limit: 1
        })
        .then(checkDuplicity)
        .then(invoiceService.put)
        .catch(q.reject);

      function checkDuplicity(result) {
        if (result.rows.length > 0) {
          return $q.reject('Cupom fiscal já está cadastrado');
        } else {
          return $q.when(document);
        }
      }

      return q.promise;
    }

    /**
     * Extract the invoice document from scanned data.
     *
     * @param data Scanned data
     * @return {Object} Document if data is valid
     */
    function extractDocument(data) {
      var parts = (data || '').split('|');

      if (parts.length < 5) {
        throw({forbidden: 'Cupom fiscal inválido para o estado de São Paulo'});
      }

      if (parts[3].length > 0) {
        throw({forbidden: 'Cupom fiscal com CPF não é permitido'});
      }

      return {
        key: extractKey(parts[0]),
        date: extractDate(parts[1]),
        value: extractValue(parts[2])
      };
    }

    /**
     * Extract the invoice key from the scanned data
     * and validates it.
     *
     * @param part String key
     * @return {Date} Document key
     */
    function extractKey(part) {
      if (part.length < 44) {
        throw({forbidden: 'Cupom fiscal com a chave inválida'});
      }

      if (part.startsWith('CFe')) {
        return part.slice(3);
      }

      return part;
    }

    /**
     * Extract the invoice date from the scanned data
     * and validates it.
     *
     * @param part String date
     * @return {Date} Document date
     */
    function extractDate(part) {
      var date = $moment(part, 'YYYY-MM-DD').toDate();

      if (date === null) {
        throw({forbidden: 'Cupom fiscal com a data inválida'});
      }

      return date;

    }

    /**
     * Extract the invoice value from the scanned data
     * and validates it.
     *
     * @param part String value
     * @return {Number} Document value
     */
    function extractValue(part) {
      var value = parseFloat(part);

      if (isNaN(value)) {
        throw({forbidden: 'Cupom fiscal com o valor inválido'});
      }

      return value;
    }

    return initialize();
  }

  service.$inject = ['$q', '$moment', 'pouch', 'invoice.Service'];
  angular.module('nccv.services').service('scanner.CodeService', service);

})();
