(function () {
  'use strict';

  function controller(NGTable, snapshotService, $toastr) {
    var self = this;

    /**
     * Public functions.
     */
    self.create = create;
    self.doExport = doExport;
    self.canExport = canExport;
    self.canDownload = canDownload;
    self.hasData = hasData;
    self.download = download;

    /**
     * Initialize this controller.
     */
    function initialize() {
      self.loaded = false;
      self.table = new NGTable({}, {
        getData: loadData,
        counts: []
      });

      return self;
    }

    /**
     * Loads initial data.
     */
    function loadData(params) {
      return snapshotService
        .findAll(params.url())
        .then(function (result) {
          self.loaded = true;
          params.total(result.data.totalElements);
          return result.data.content;
        });
    }

    /**
     * Create a snapshot.
     */
    function create() {
      function handleSuccess() {
        $toastr.success('Snapshot criado');
        self.table.reload();
      }

      return snapshotService.create()
        .then(handleSuccess);
    }

    /**
     * Notify devices and create a downloadable file.
     */
    function doExport(snapshot) {
      function handleSuccess() {
        $toastr.success('Snapshot processado');
        self.table.reload();
      }

      return snapshotService
        .doExport(snapshot)
        .then(handleSuccess);
    }

    /**
     * Download the created snapshot.
     */
    function download(snapshot) {
      snapshotService.download(snapshot, 'key')
        .then(function (result) {
          download(result, 'CHAVE');
        });

      snapshotService.download(snapshot, 'coo')
        .then(function (result) {
          download(result, 'COO');
        });

      function download(result, prefix) {
        var keyBlob = new Blob([result.data], {type: "text/csv;charset=utf-8"});
        saveAs(keyBlob, 'NotasCsv-' + prefix + '-' + snapshot.id + '.csv');
      }
    }

    /**
     * Check if the file can be exported.
     */
    function canExport(snapshot) {
      return snapshot.status === 'COMPLETED';
    }

    /**
     * Check if the file can be downloaded.
     */
    function canDownload(snapshot) {
      return snapshot.status === 'EXPORTED';
    }

    /**
     * Check if exists any data from server.
     */
    function hasData() {
      return self.table.total() > 0;
    }

    return initialize();
  }

  controller.$inject = ['NgTableParams', 'snapshot.Service', 'toastr'];
  angular.module('nccv.controllers').controller('snapshot.ListController', controller);
})();