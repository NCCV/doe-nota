(function () {
  'use strict';

  function interceptor($q, $rootScope, $toastr) {
    return {
      response: function(response) {
        return response;
      },
      responseError: function(response) {
        var config = response.config;

        if (response.status === -1) {
          $toastr.error('Não foi possível conectar ao servidor', 'Oops');
          return $q.reject(response)
        }

        if (config.bypassError) {
          return $q.reject(response);
        }

        if (response.data && response.data.message) {
          $toastr.error(response.data.message, 'Oops');
        }

        $rootScope.$broadcast("app.httpError", response.data);
        return $q.reject(response);
      }
    };
  }

  interceptor.$inject = ['$q', '$rootScope', 'toastr'];
  angular.module('nccv.interceptors').factory('httpErrorInterceptor', interceptor);
  angular.module('nccv.interceptors').config(['$httpProvider', function ($httpProvider) {
    $httpProvider.interceptors.push('httpErrorInterceptor');
  }]);

})();