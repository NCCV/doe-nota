(function () {

  /**
   * Handle permission on device.
   */
  function service($q) {
    var self = this;

    /**
     * Public constants.
     */
    self.CAMERA = 'android.permission.CAMERA';

    /**
     * Public functions.
     */
    self.ask = ask;

    /**
     * Initialize this service.
     *
     * @returns {Object} this service
     */
    function initialize() {
      return self;
    }

    /**
     * Ask to the underline device if user
     * can access the resource.
     */
    function ask(permission) {
      if (!ionic.Platform.isAndroid()) {
        return $q.when();
      }

      var q = $q.defer();
      var permissions = cordova.plugins.permissions;
      permissions.hasPermission(permission, checkPermission, q.reject);

      function checkPermission(status) {
        if (status.hasPermission) {
          q.resolve();
        } else {
          permissions.requestPermission(permissions.CAMERA, resolve, q.reject);
        }
      }

      function resolve(status) {
        if (status.hasPermission) {
          q.resolve();
        } else {
          q.reject();
        }
      }

      return q.promise;
    }

    return initialize();
  }

  service.$inject = ['$q'];
  angular.module('nccv.services').service('$permission', service);

})();
