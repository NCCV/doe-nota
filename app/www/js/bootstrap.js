(function () {
  'use strict';

  angular.module('nccv.configurations', []);
  angular.module('nccv.constants', []);
  angular.module('nccv.controllers', []);
  angular.module('nccv.directives', []);
  angular.module('nccv.services', []);
  angular.module('nccv', [
    'ionic',
    'nccv.configurations',
    'nccv.constants',
    'nccv.controllers',
    'nccv.directives',
    'nccv.services',
    'ngCordova',
    'angular-momentjs',
    'ui.utils.masks',
    'templates'
  ]);

})();
