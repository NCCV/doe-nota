(function () {
  'use strict';

  angular.element(document).ready(function () {
    window.keycloak = Keycloak('configuration/keycloak.json');

    keycloak.init({
      onLoad: 'login-required'
    })
    .success(function (authenticated) {
      if(authenticated) {
        keycloak.loadUserProfile()
            .success(function(profile){
              sessionStorage.setItem('nccv_user_profile', JSON.stringify(profile));
              angular.bootstrap(document, ['nccv']);
            });
      }
      else {
        window.location.reload();
      }
    })
    .error(function() {
      window.location.reload();
    });
  });

})();
