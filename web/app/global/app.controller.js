(function () {
  'use strict';

  function controller() {
    var self = this;
    var profile = undefined;

    /**
     * Public functions.
     */
    self.logout = logout;
    self.manageAccount = manageAccount;
    self.userProfile = userProfile;

    /**
     * Initialize this controller.
     */
    function initialize() {
      return self;
    }

    /**
     * Executes the logout from application.
     */
    function logout() {
      keycloak.logout();
    }

    /**
     * Manage the user account
     */
    function manageAccount() {
      keycloak.accountManagement();
    }

    /**
     * Find the user first name.
     */
    function userProfile() {
      if (angular.isUndefined(profile)) {
        var profileJson = sessionStorage.getItem('nccv_user_profile');

        if (profileJson) {
          profile = JSON.parse(profileJson);
        }
      }

      return profile;
    }

    return initialize();
  }

  controller.$inject = [];
  angular.module('nccv.controllers').controller('app.Controller', controller);
})();