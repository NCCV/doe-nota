(function () {

  /**
   * Hold validation functions for the app.
   */
  function service() {
    var self = this;

    /**
     * Public functions.
     */
    self.isCooValid = isCooValid;

    /**
     * Check is the COO number is valid.
     *
     * @param coo number
     * @returns {boolean} true if valid, false otherwise
     */
    function isCooValid(coo) {
      var rawCoo = coo
        ? coo.toString().replace(/[^\d]+/g, '')
        : '';

      return ((rawCoo !== '') && (rawCoo.length === 6));
    }
  }

  service.$inject = [];
  angular.module('nccv.services').service('validation', service);

})();
