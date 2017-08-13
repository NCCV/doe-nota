(function () {
  'use strict';

  /**
   * Responsible for controlling the scanner tab.
   */
  function controller($scope, $rootScope, $ionicActionSheet, $cordovaToast, $cordovaDatePicker, $ionicModal,
    $moment, $ionicLoading, scandit, ocr, codeService, ocrService
  ) {
    var self = this;

    /**
     * Public functions.
     */
    self.start = start;

    /**
     * Scope functions.
     */
    $scope.saveNfe = saveNfe;
    $scope.pickDate = pickDate;

    /**
     * Initialize the controller.
     *
     * @returns {Object} This instance
     */
    function initialize() {
      $rootScope.$on(scandit.SCANNED_EVENT, onScanditResult);
      $rootScope.$on(ocr.SCANNED_EVENT, onOcrResult);

      return self;
    }

    /**
     * Initialize the action sheet for the user.
     */
    function start() {
      $ionicActionSheet.show({
        titleText: 'Escanear notas',
        buttons: [
          {text: '<i class="icon ion-image"></i> Sem código'},
          {text: '<i class="icon ion-qr-scanner"></i> Com QR code'},
          {text: '<i class="icon ion-android-list"></i> Cadastrar sem escanear'}
        ],
        buttonClicked: function (index) {
          switch (index) {
            case 0: {
              ocr.scan();
              break;
            }

            case 1: {
              scandit.scan();
              break;
            }

            case 2: {
              captureNfe();
              break;
            }
          }

          return true;
        }
      });
    }

    /**
     * Initialize the NFe modal.
     *
     * @param rawDocument Current document with scanned data
     */
    function captureNfe(rawDocument) {
      var nfeDocument = angular.copy(rawDocument || {});
      $scope.startNfeAutomatically = angular.isDefined(rawDocument);

      if (!nfeDocument.value) {
        nfeDocument.value = 0;
      }

      if (nfeDocument.date) {
        nfeDocument.formattedDate =
          $moment(nfeDocument.date).format('DD/MM/YYYY');
      }

      $scope.document = nfeDocument;
      $ionicModal.fromTemplateUrl('templates/nfe-modal.html', {
        scope: $scope,
        animation: 'slide-in-up'
      }).then(function (modal) {
        $scope.modal = modal;
        $scope.modal.show();
      });
    }

    /**
     *
     * @param nfeDocument
     */
    function saveNfe(nfeDocument) {
      ocrService.save(nfeDocument)
        .then(onSuccess)
        .catch($cordovaToast.showLongBottom);

      function onSuccess() {
        $scope.modal.hide()
          .then(checkReloading);
      }

      function checkReloading() {
        if ($scope.startNfeAutomatically) {
          ocr.scan();
        }
      }
    }

    /**
     * Show the date picker.
     *
     * @param date Current date
     */
    function pickDate(date) {
      var options = {
        date: date || new Date(),
        mode: 'date'
      };

      $cordovaDatePicker.show(options)
        .then(function (date) {
          $scope.document.date = date;
          $scope.document.formattedDate = $moment(date).format('DD/MM/YYYY');
        });
    }

    /**
     * Handle the scanned data from the invoice using Scandit.
     *
     * @param event Angular event data
     * @param data Scanned data
     */
    function onScanditResult(event, data) {
      codeService.process(event, data)
        .catch(showError);

      function showError(error) {
        scandit.cancel()
          .then(function () {
            $cordovaToast.showLongBottom(error);
          });
      }
    }

    /**
     * Handle the scanned data from the invoice using OCR.
     *
     * @param event Angular event data
     * @param data Scanned data
     */
    function onOcrResult(event, data) {
      if (ocrService.accept(data)) {
        acceptScan();
      } else {
        showNfeModal();
      }

      function acceptScan() {
        ocrService.process(event, data)
          .then($ionicLoading.hide)
          .then(ocr.scan)
          .catch(showNfeModal);
      }

      function showNfeModal(result) {
        $ionicLoading.hide()
          .then(function () {
            if (result && result.document) {
              captureNfe(result.document);
            } else {
              captureNfe({});
            }
          });
      }


    }

    return initialize();
  }

  controller.$inject = [
    '$scope', '$rootScope', '$ionicActionSheet', '$cordovaToast', '$cordovaDatePicker', '$ionicModal',
    '$moment', '$ionicLoading', 'scandit', 'ocr', 'scanner.CodeService', 'scanner.OcrService'
  ];

  angular.module('nccv.controllers').controller('scanner.Controller', controller);

})();
