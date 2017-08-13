(function () {

  function service(App, $http) {
    var self = this;

    /**
     * Public functions.
     */
    self.create = create;
    self.findAll = findAll;
    self.doExport = doExport;
    self.download = download;

    /**
     * Initialize this service.
     */
    function initialize() {
      return self;
    }

    /**
     * Create a snapshot.
     */
    function create() {
      return $http({
        url: App.url + '/snapshots',
        method: 'POST'
      });
    }

    /**
     * Find all snapshots.
     */
    function findAll(params) {
      return $http({
        url: App.url + '/snapshots',
        method: 'GET',
        params: {
          page: params.page - 1,
          size: params.count,
          sort: 'createdAt,desc'
        }
      });
    }

    /**
     * Export the snapshot.
     */
    function doExport(snapshot) {
      return $http({
        url: App.url + '/snapshots/' + snapshot.id + '/exports',
        method: 'POST'
      });
    }

    /**
     * Download the created snapshot.
     */
    function download(snapshot, qualifier) {
      return $http({
        url: App.url + '/snapshots/' + snapshot.id,
        method: 'GET',
        headers: {
          'Accept': 'text/csv'
        },
        params: {
          qualifier: qualifier
        }
      });
    }

    return initialize();
  }

  service.$inject = ['App', '$http'];
  angular.module('nccv.services').service('snapshot.Service', service);

})();