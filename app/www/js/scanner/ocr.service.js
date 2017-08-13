(function () {
  'use strict';

  /**
   * Responsible for processing ocr based data.
   */
  function service($q, $moment, $cordovaVibration, validation, pouch, invoiceService) {
    var self = this;

    /**
     * Public functions.
     */
    self.accept = accept;
    self.process = process;
    self.save = save;

    /**
     * Initialize this service.
     *
     * @return {Object} This service
     */
    function initialize() {
      return self;
    }

    /**
     * Check if the data can be processed by this service.
     */
    function accept(data) {
      return angular.isDefined(data)
        && angular.isDefined(data.ParsedResults)
        && data.ParsedResults.length > 0
        && data.ParsedResults[0].ParsedText !== '';
    }

    /**
     * Process the data from a specific event.
     *
     * @param event Event information
     * @param data Event data from scanner
     */
    function process(event, data) {
      var parsedResult = data.ParsedResults[0];
      var document = extractDocument(parsedResult);

      if (!findAnyMissingField(document)) {
        return save(document);
      } else {
        return $q.reject({
          document: document
        });
      }
    }

    /**
     * Save the document to the database.
     *
     * @param rawDocument Document to be saved.
     * @returns Promise to be resolved later
     */
    function save(rawDocument) {
      var q = $q.defer();
      var document = angular.copy(rawDocument);
      document.status = 'scanned';
      delete document.formattedDate;

      try {
        validate(document);
      } catch (ex) {
        return $q.reject(ex.forbidden);
      }

      pouch
        .findAll('_invoice/coo', {
          key: document.coo, limit: 1
        })
        .then(checkDuplicity)
        .then(invoiceService.put)
        .then(notifySuccess)
        .catch(q.reject);

      function checkDuplicity(result) {
        if (result.rows.length > 0) {
          return $q.reject('Cupom fiscal já está cadastrado');
        } else {
          return $q.when(document);
        }
      }

      function notifySuccess() {
        $cordovaVibration.vibrate(100);
        q.resolve();
      }

      return q.promise;
    }

    /**
     * Validate the document including required
     * and valid fields.
     *
     * @param document
     */
    function validate(document) {
      if (!document.cnpj) {
        throw({forbidden: 'Informe um CNPJ válido'});
      }

      if (!document.date) {
        throw({forbidden: 'Informe a data'});
      }

      if (!document.coo) {
        throw({forbidden: 'Informe o COO'});
      }

      if (!validation.isCooValid(document.coo)) {
        throw({forbidden: 'COO inválido'});
      }

      if (!document.value || document.value == 0) {
        throw({forbidden: 'Informe o valor'});
      }
    }

    /**
     * Check document missing required data.
     *
     * @param document extracted from the parsed text
     * @returns {Array} With missing fields
     */
    function findAnyMissingField(document) {
      if (!document.cnpj) {
        return true;
      }

      if (!document.coo) {
        return true;
      }

      if (!document.date) {
        return true;
      }

      if (!document.value) {
        return true;
      }

      return false;
    }

    /**
     * Extract the document from the parsed text.
     *
     * @param data info with the parsed text
     * @returns {Object} document with data or missing attributes
     */
    function extractDocument(data) {
      return {
        cnpj: extractCnpj(data.ParsedText),
        coo: extractCoo(data.ParsedText),
        date: extractDate(data.ParsedText),
        value: extractValue(data.TextOverlay)
      };
    }

    /**
     * Extract CNPJ from the parsed text.
     *
     * @param text to search CNPJ in
     * @returns {String} CNPJ extracted
     */
    function extractCnpj(text) {
      var matches = /([0-9]{2}[\.]?[0-9]{3}[\.]?[0-9]{3}[\/]?[0-9]{4}[-]?[0-9]{2})|([0-9]{3}[\.]?[0-9]{3}[\.]?[0-9]{3}[-]?[0-9]{2})/.exec(text);
      var cnpj = undefined;

      if (matches !== null && matches.length > 0) {
        if (matches[0].length === 18) {
          cnpj = matches[0];
        }
      }

      return cnpj;
    }

    /**
     * Extract coo from the text.
     *
     * @param text to find the coo
     * @returns the coo extracted from the text
     */
    function extractCoo(text) {
      var matches = /coo\s*\:\s*(\d{6})/i.exec(text);
      var coo = undefined;

      if (matches !== null && matches.length > 0) {
        var rawCoo = matches[0].replace(/\D/g, '');

        if (rawCoo.length === 6) {
          coo = rawCoo;
        }
      }

      return coo;
    }

    /**
     * Extract date from the parsed text.
     *
     * @param text to search date on
     * @returns date from the text
     */
    function extractDate(text) {
      var matches = /(\d{2}\/\d{2}\/\d{4})/.exec(text);
      var date = undefined;

      if (matches !== null && matches.length > 0) {
        date = $moment(matches[0], 'DD/MM/YYYY').toDate();
      }

      return date;
    }

    /**
     * Organize words to search the total.
     *
     * @param overlay text info from the parsing
     * @returns invoice value
     */
    function extractValue(overlay) {
      var value = undefined;
      var words = [];

      overlay.Lines.forEach(function (line) {
        line.Words.forEach(function (word) {
          words.push(word);
        })
      });

      words.sort(function (wordA, wordB) {
        return wordA.Top - wordB.Top;
      });

      for (var index = 0; index < words.length; index++) {
        var word = words[index];
        var lowerText = word.WordText.toLowerCase();

        if (lowerText.indexOf('total') > -1) {
          var length = words.length - (index + 1);
          value = extractNextValue(words.slice(index + 1, length));
          break;
        }
      }

      return value;
    }

    /**
     * Extract the first value after the total word.
     *
     * @param words Words to search the value in
     * @returns value inside the words
     */
    function extractNextValue(words) {
      var value = undefined;

      for (var index = 0; index < words.length; index++) {
        var matches = /\d+[,.]\d{2}/g.exec(words[index].WordText);

        if (matches !== null && matches.length > 0) {
          value = convertFloat(matches[0]);
          break;
        }
      }

      return value;
    }

    /**
     * Convert float from string value.
     *
     * @param value value from string
     * @returns converted value
     */
    function convertFloat(value) {
      if (value === '') {
        value = 0;
      } else {
        value = value.replace('.', '');
        value = value.replace(',', '.');
        value = parseFloat(value);
      }

      return value;
    }

    return initialize();
  }

  service.$inject = ['$q', '$moment', '$cordovaVibration', 'validation', 'pouch', 'invoice.Service'];
  angular.module('nccv.services').service('scanner.OcrService', service);

})();
