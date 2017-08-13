(function () {
  'use strict';

  angular.module('nccv.constants')
    .constant('App', {
      COUCH_URL: '@@couchUrl',
      PROFILE: '@@profile',
      TOKEN: '@@token',
      SCANDIT_API_KEY: '@@scanditApiKey',
      SCANDIT_CODE_DUPLICATE_FILTER: '@@scanditCodeDuplicateFilter',
      OCR_URL: '@@ocrUrl',
      OCR_KEY: '@@ocrKey'
    })
})();
