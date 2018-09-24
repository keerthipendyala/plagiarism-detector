/**
 * This controller takes care of all report related activities
 */
(function () {
    angular.module("CopyCatch")
        .controller("reportController", reportController);

    function reportController(submissionService, $routeParams, userService, $window) {
        var vm = this;
        vm.repId = $routeParams['rid'];


        /**
         * init method is called every time the controller is loaded and initiates the user object.
         */
        var init = function () {
            submissionService
                .getReportLinkById(vm.repId)
                .then(function (link) {
                    $window.open(link.url, '_blank');
                });

        };
        init();


    }
})();