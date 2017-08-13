(function () {
  'use strict';

  /**
   * Platform configuration.
   */
  function run($ionicPlatform) {
    $ionicPlatform.ready(function () {
      if (angular.isDefined(window.cordova)
          && angular.isDefined(window.cordova.plugins)
          && angular.isDefined(window.cordova.plugins.Keyboard)) {
        cordova.plugins.Keyboard.hideKeyboardAccessoryBar(true);
        cordova.plugins.Keyboard.disableScroll(true);
      }

      if (angular.isDefined(window.StatusBar)) {
        StatusBar.styleDefault();
        StatusBar.backgroundColorByHexString("#125f63");

        if (ionic.Platform.isIOS()) {
          StatusBar.overlaysWebView(false);
        }
      }

      if (angular.isDefined(navigator.splashscreen)) {
        navigator.splashscreen.hide();
      }
    });
  }

  /**
   * Initialization of resources.
   */
  function configure($ionicConfigProvider) {
    $ionicConfigProvider.tabs.position('bottom');
  }

  run.$inject = ['$ionicPlatform'];
  configure.$inject = ['$ionicConfigProvider'];

  angular.module('nccv.configurations').run(run);
  angular.module('nccv.configurations').config(configure);

})();
