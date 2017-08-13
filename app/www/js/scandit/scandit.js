(function () {
  'use strict';

  /**
   * Responsible for wrapping scandit.
   */
  function service(App, $q, $rootScope, $timeout, $ionicPlatform) {
    var self = this;
    var picker = undefined;

    /**
     * Static attributes.
     */
    self.SCANNED_EVENT = 'scandit.scanned';

    /**
     * Public functions.
     */
    self.scan = scan;
    self.cancel = cancel;

    /**
     *  Initialize the service.
     *
     * @return {Object} This service
     */
    function initialize() {
      if (!ionic.Platform.is('win32')) {
        $ionicPlatform.ready(configure);
      }

      return self;
    }

    /**
     * Create and configure the Scandit.
     */
    function configure() {
      Scandit.License.setAppKey(App.SCANDIT_API_KEY);

      var settings = new Scandit.ScanSettings();
      settings.setSymbologyEnabled(Scandit.Barcode.Symbology.QR, true);
      settings.setSymbologyEnabled(Scandit.Barcode.Symbology.CODE128, true);
      settings.codeDuplicateFilter = parseInt(App.SCANDIT_CODE_DUPLICATE_FILTER);

      picker = new Scandit.BarcodePicker(settings);
      picker.continuousMode = true;
    }

    /**
     * Start scanning barcode.
     */
    function scan() {
      picker.show({
        didScan: whenScanned,
        didCancel: whenCancelled
      });

      picker.startScanning();
    }

    /**
     * Cancel scanning barcode.
     */
    function cancel() {
      var q = $q.defer();
      var turnOffListener = $rootScope
        .$on('scandit.cancelled', onCancelled);

      function onCancelled() {
        turnOffListener();
        $timeout(q.resolve, 500);
      }

      picker.cancel();
      return q.promise;
    }

    /**
     * Callback for scandit success result.
     *
     * @param result Result from scanning
     */
    function whenScanned(result) {
      $rootScope.$emit('scandit.scanned', result.newlyRecognizedCodes[0].data);
    }

    /**
     * Callback for scandit cancel result.
     */
    function whenCancelled() {
      $rootScope.$emit('scandit.cancelled');
    }

    return initialize();
  }

  service.$inject = ['App', '$q', '$rootScope', '$timeout', '$ionicPlatform'];
  angular.module('nccv.services').service('scandit', service);

})();
