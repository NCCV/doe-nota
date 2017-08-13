(function () {
  'use strict';

  function configuration($stateProvider, $urlRouterProvider) {
    $stateProvider
      .state('dashboard', {
        url: '/dashboard',
        templateUrl: '/app/dashboard/dashboard.html',
        controller: 'dashboard.Controller as vm'
      })
      .state('snapshots', {
        url: '/snapshots',
        templateUrl: '/app/snapshot/list.html',
        controller: 'snapshot.ListController as vm'
      });

    $urlRouterProvider.otherwise('/dashboard');
  }

  configuration.$inject = ['$stateProvider', '$urlRouterProvider'];
  angular.module('nccv').config(configuration);

})();
