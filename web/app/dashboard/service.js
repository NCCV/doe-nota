(function () {

  function service(App, $http) {
    var self = this;

    /**
     * Public functions.
     */
    self.get = get;

    /**
     * Initialize this service.
     */
    function initialize() {
      return self;
    }

    /**
     * Find all snapshots.
     */
    function get() {
      return $http({
        url: App.url + '/dashboards/DEFAULT',
        method: 'GET'
      });
    }

    return initialize();
  }

  service.$inject = ['App', '$http'];
  angular.module('nccv.services').service('dashboard.Service', service);

})();