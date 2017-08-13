(function () {
  'use strict';

  angular.module('nccv.configurations', []);
  angular.module('nccv.constants', []);
  angular.module('nccv.controllers', []);
  angular.module('nccv.directives', []);
  angular.module('nccv.interceptors', []);
  angular.module('nccv.services', []);
  angular.module('nccv', [
    'nccv.configurations',
    'nccv.constants',
    'nccv.controllers',
    'nccv.directives',
    'nccv.interceptors',
    'nccv.services',
    'ui.router',
    'toastr',
    'ngTable',
    'chart.js'
  ]);

})();
