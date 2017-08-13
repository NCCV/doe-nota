(function () {
  'use strict';

  /**
   * Configures routing.
   */
  function configuration($stateProvider, $urlRouterProvider) {
    $stateProvider
      .state('tab', {
        url: '/tab',
        abstract: true,
        templateUrl: 'templates/tabs.html'
      })
      .state('tab.invoices', {
        url: '/invoices',
        views: {
          'tab-invoices': {
            templateUrl: 'templates/tab-invoices.html',
            controller: 'invoice.Controller as vm'
          }
        }
      })
      .state('tab.scanner', {
        url: '/scanner',
        views: {
          'tab-scanner': {
            templateUrl: 'templates/tab-scanner.html',
            controller: 'scanner.Controller as vm'
          }
        }
      });

    $urlRouterProvider.otherwise('/tab/scanner');
  }

  configuration.$inject = ['$stateProvider', '$urlRouterProvider'];
  angular.module('nccv').config(configuration);

})();
