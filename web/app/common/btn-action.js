(function () {
  'use strict';

  function directive($compile) {
    return {
      restrict: 'A',
      scope: {
        btnAction: '&',
        hideContent: '@'
      },
      link: function ($scope, $element) {
        $scope.state = 'idle';
        $scope.wrapperStyle = {}
        $scope.execute = execute;

        $element.removeAttr('btn-action');
        $element.attr('ng-click', 'execute()');
        $element.html([
          '<span ng-hide="state == \'progress\' && hideContent" ng-style="wrapperStyle">' + $element[0].innerHTML + '</span>',
          '<span class="fa fa-circle-o-notch fa-spin ng-hide" ng-show="state == \'progress\'"></span>',
          '<span class="fa fa-check ng-hide" ng-show="state == \'success\' && !hideContent"></span>',
          '<span class="fa fa-times ng-hide" ng-show="state == \'error\' && !hideContent"></span>',
        ].join(''));

        function execute() {
          $scope.state = 'progress';
          $element.attr('disabled', 'disabled');

          if (!$scope.hideContent) {
            $scope.wrapperStyle = {
              'padding-right': '6px'
            }
          }

          $scope.btnAction()
            .then(function () {
              $scope.state = 'success';
            })
            .catch(function () {
              $scope.state = 'error';
            })
            .finally(function() {
              $element.removeAttr('disabled');
            });
        }

        $compile($element)($scope);
      }
    };
  }

  directive.$inject = ['$compile'];
  angular.module('nccv.directives').directive('btnAction', directive);
})();

