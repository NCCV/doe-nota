(function () {
  'use strict';

  /**
   * Responsible for wrapping ocr.
   */
  function service(App, $q, $rootScope, $http, $permission, $cordovaFile, $cordovaToast, $ionicLoading) {
    var self = this;

    /**
     * Static attributes.
     */
    self.SCANNED_EVENT = 'ocr.scanned';

    /**
     * Private variables.
     */
    var cameraSettings = {
      targetWidth: 720
    };

    /**
     * Public functions.
     */
    self.scan = scan;

    /**
     *  Initialize the service.
     *
     * @return {Object} This service
     */
    function initialize() {
      return self;
    }

    /**
     * Start getting image from camera.
     */
    function scan() {
      $permission.ask($permission.CAMERA)
        .then(takePicture)
        .catch(showError);

      function takePicture() {
        var filename = Math.uuid().toLowerCase() + '.jpg';
        navigator.customCamera.getPicture(filename, processImage, showError, cameraSettings);
      }

      function showError() {
        $cordovaToast.showLongBottom('Não foi possível iniciar a câmera');
      }
    }

    /**
     * Process the image from the camera, reading it and sending
     * to the OCR service to data extraction.
     */
    function processImage(path) {
      var lastIndex = path.lastIndexOf("/");
      var directory = path.substr(0, lastIndex + 1);
      var filename = path.substr(lastIndex + 1, directory.length);

      $ionicLoading.show()
        .then(function () {
          return readImage(directory, filename);
        })
        .then(parseImage)
        .then(processOcrResult);
    }

    /**
     * Read file from the device through the informed path.
     *
     * @param directory where file is
     * @param filename to upload
     * @returns {Promise} to be resolved later
     */
    function readImage(directory, filename) {
      var q = $q.defer();

      $cordovaFile
        .readAsArrayBuffer(directory, filename)
        .then(resolveAsImage)
        .catch(q.reject);

      function resolveAsImage(content) {
        q.resolve({
          image: new Blob([content], { type: "image/jpeg" }),
          filename: filename
        });
      }

      return q.promise;
    }

    /**
     * Parse the image using the OCR service.
     *
     * @param content Image
     * @returns {Promise}
     */
    function parseImage(content) {
      var q = $q.defer();

      var payload = new FormData();
      payload.append('file', content.image, content.filename);
      payload.append('apikey', App.OCR_KEY);
      payload.append('language', 'por');
      payload.append('isOverlayRequired', 'true');

      $http({
        url: App.OCR_URL,
        method: 'POST',
        data: payload,
        headers: {
          'Content-Type': undefined
        },
        transformRequest: angular.identity
      })
      .then(q.resolve)
      .catch(function (e) {
        console.log(JSON.stringify(e));
      });

      return q.promise;
    }

    /**
     * Callback for ocr success result.
     *
     * @param result Result from scanning
     */
    function processOcrResult(result) {
      var data = result ? result.data : undefined;
      $rootScope.$emit('ocr.scanned', data);
    }

    return initialize();
  }

  service.$inject = ['App', '$q', '$rootScope', '$http', '$permission', '$cordovaFile', '$cordovaToast', '$ionicLoading'];
  angular.module('nccv.services').service('ocr', service);

})();
