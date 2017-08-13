(function () {
  'use strict';

  /**
   * Responsible for controlling the invoice tab.
   */
  function controller($ionicPlatform, $rootScope, $scope, invoiceService) {
    var self = this;

    /**
     * Public properties.
     */
    self.invoices = [];

    /**
     * Public functions.
     */
    self.load = load;

    /**
     * Initialize this controller.
     *
     * @return {controller} This controller
     */
    function initialize() {
      $scope.$on('$ionicView.afterEnter', load);
      return self;
    }

    /**
     * Load invoices.
     */
    function load() {
      invoiceService
        .findAll('date', {
          order: 'desc'
        })
        .then(assign)
        .finally(release);

      function assign(invoices) {
        self.invoices = invoices;
      }

      function release() {
        $rootScope.$broadcast('scroll.refreshComplete')
      }
    }

    return initialize();
  }

  controller.$inject = ['$ionicPlatform', '$rootScope', '$scope', 'invoice.Service'];
  angular.module('nccv.controllers').controller('invoice.Controller', controller);

})();
