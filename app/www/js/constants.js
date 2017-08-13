(function () {
  'use strict';

  angular.module('nccv.constants')
    .constant('App', {
      COUCH_URL: 'http://couch.nccv.org.br',
      PROFILE: 'production',
      TOKEN: 'a2cae4a6e7d84a6ebd5e8d553e7ca02f',
      SCANDIT_API_KEY: 'RZ8C8lwMTP2EV2+eKlR71fTK0ZChnKdO+MkozkfQnKo',
      SCANDIT_CODE_DUPLICATE_FILTER: '8000',
      OCR_URL: 'https://api.ocr.space/parse/image',
      OCR_KEY: '5918f349f688957'
    })
})();
