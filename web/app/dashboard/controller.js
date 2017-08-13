(function () {
  'use strict';

  function controller(dashboardService) {
    var self = this;

    /**
     * Properties
     */
    self.invoiceTotalByMonth = {
      labels: [], data: []
    };

    self.invoiceTotalByYear = {
      labels: [], data: []
    };

    /**
     * Initialize this controller.
     */
    function initialize() {
      dashboardService.get()
        .then(mountDashboard);

      return self;
    }

    function mountDashboard(result) {
      var dashboard = result.data;

      dashboard.invoiceTotalByMonth.forEach(function (item) {
        self.invoiceTotalByMonth.labels.push(item.date);
        self.invoiceTotalByMonth.data.push(item.value);
      });

      dashboard.invoiceTotalByYear.forEach(function (item) {
        self.invoiceTotalByYear.labels.push(item.date);
        self.invoiceTotalByYear.data.push(item.value);
      });
    }

    return initialize();
  }

  controller.$inject = ['dashboard.Service'];
  angular.module('nccv.controllers').controller('dashboard.Controller', controller);
})();