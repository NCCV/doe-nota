(function () {
  'use strict';

  /**
   * Responsible for wrapping pouch service.
   */
  function service(App, PouchDesign, $q, $http, $ionicPlatform, $cordovaDevice) {
    var self = this;
    var localDatabase = undefined;
    var remoteDatabase = undefined;
    var pouchInfo = undefined;

    /**
     * Public functions.
     */
    self.save = save;
    self.findById = findById;
    self.findAll = findAll;

    /**
     *  Initialize the service.
     *
     * @return {Object} This service
     */
    function initialize() {
      $ionicPlatform.ready(function () {
        createPouchInfo();
        createDatabase();
      });

      return self;
    }

    /**
     * Save document to database using put. If a document ID was not
     * provided, an UUID will be set.
     *
     * @param document Document to be saved
     * @return {Promise} Promise to be resolved later
     */
    function save(document) {
      var q = $q.defer();

      if (angular.isUndefined(document._id)) {
        document._id = Math.uuid().toLowerCase();
      }

      localDatabase.put(document)
        .then(q.resolve)
        .catch(q.reject);

      return q.promise;
    }

    /**
     * Find one document by its id.
     *
     * @param id Document id
     * @return {Promise} Promise to be resolved later
     */
    function findById(id) {
      var q = $q.defer();

      localDatabase.get(id)
        .then(q.resolve)
        .catch(q.reject);

      return q.promise;
    }

    /**
     * Find all documents by design.
     *
     * @param name Name of the design document
     * @param options Options to apply to the query
     * @return {Promise} Promise to be resolved later
     */
    function findAll(name, options) {
      var q = $q.defer();

      localDatabase.query(name, options)
        .then(q.resolve)
        .catch(q.reject);

      return q.promise;
    }

    /**
     * Create local and remote databases.
     */
    function createDatabase() {
      localDatabase = new PouchDB(pouchInfo.databaseName, {
        adapter: 'websql',
        iosDatabaseLocation: 'default'
      });

      remoteDatabase = new PouchDB(pouchInfo.databaseEndpoint, {
        skipSetup: true,
        ajax: {
          headers: {
            'Authorization': pouchInfo.authorization
          }
        }
      });

      var designResults = PouchDesign.ALL.map(function (design) {
        return save(design);
      });

      $q.all(designResults).finally(trySynchronize);
    }

    /**
     * Starts the live synchronization after try login and register if
     * was the first access.
     */
    function trySynchronize() {
      $http({
        method: 'GET',
        url: pouchInfo.userEndpoint,
        headers: {
          'Authorization': pouchInfo.authorization
        }
      })
      .then(synchronize)
      .catch(tryRegister);

      function synchronize() {
        localDatabase.sync(remoteDatabase, {
          live: true, retry: true
        });
      }

      function tryRegister() {
        $http({
          method: 'PUT',
          url: pouchInfo.userEndpoint,
          data: {
            _id: pouchInfo.userId,
            name: pouchInfo.username,
            password: pouchInfo.password,
            roles: [],
            type: 'user'
          }
        });
      }
    }

    /**
     * Initialize all information needed by pouch.
     */
    function createPouchInfo() {
      pouchInfo = {
        mobileApp: !ionic.Platform.is('win32')
      };

      if (pouchInfo.mobileApp) {
        pouchInfo.uuid = $cordovaDevice.getUUID();
      } else {
        pouchInfo.uuid = App.PROFILE;
      }

      pouchInfo.userId = 'org.couchdb.user:' + pouchInfo.uuid;
      pouchInfo.username = pouchInfo.uuid;
      pouchInfo.password = createPassword(pouchInfo);
      pouchInfo.authorization = 'Basic ' + btoa(pouchInfo.username + ':' + pouchInfo.password);
      pouchInfo.databaseName = 'nccv-' + pouchInfo.uuid;
      pouchInfo.databaseEndpoint = App.COUCH_URL + '/' + pouchInfo.databaseName;
      pouchInfo.userEndpoint = App.COUCH_URL + '/_users/' + pouchInfo.userId;
    }

    /**
     * Generate a device password based on token and device information.
     *
     * @param pouchInfo Information needed by pouch
     * @return {string} Special password
     */
    function createPassword(pouchInfo) {
      var password = [
        App.TOKEN, pouchInfo.uuid
      ];

      if (pouchInfo.mobileApp) {
        password.push($cordovaDevice.getPlatform());
        password.push($cordovaDevice.getModel());
      }

      return password.join(':');
    }

    return initialize();
  }

  service.$inject = ['App', 'PouchDesign', '$q', '$http', '$ionicPlatform', '$cordovaDevice'];
  angular.module('nccv.services').service('pouch', service);

})();
