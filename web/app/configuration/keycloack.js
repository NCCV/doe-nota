(function () {
  'use strict';

  function configuration($httpProvider) {
    var isExpired = keycloak.isTokenExpired();
    var token = keycloak.token;

    if (isExpired) {
      keycloak.updateToken(5)
        .success(function () {
          $httpProvider.defaults.headers.common['Authorization'] = 'Bearer ' + token;
        });
    }

    $httpProvider.defaults.headers.common['Authorization'] = 'Bearer ' + token;
  }

  configuration.$inject = ['$httpProvider'];
  angular.module('nccv').config(configuration);

})();