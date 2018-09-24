/**
 * This controller helps with activities related to viewing assignment submission history
 */
(function () {
    angular.module("CopyCatch")
        .controller("subHistoryController", subHistoryController);

    function subHistoryController(userService, submissionService, $location, $routeParams) {
        var vm = this;
        vm.logout = logout;
        vm.stuId = $routeParams['sid'];
        vm.courseid = $routeParams['cid'];
        vm.aid = $routeParams['aid'];
        vm.goBack = goBack;
        vm.coursename = $routeParams['cname'];
        vm.newAssg = {};

        /**
         * logs out user from the session
         */
        function logout() {
            userService.logout()
                .then(
                    $location.url("/login"));
        }

        /**
         * navigates back to the previous page
         */
        function goBack() {
            history.go(-1);
        }

        /**
         * init method is called every time the controller is loaded and initiates the user object.
         */
        function init() {
            vm.error = "";
            vm.canUpload = false;
            submissionService
                .subHistory(vm.stuId, vm.courseid, vm.aid)
                .then(function (assgs) {
                    if (assgs !== undefined) {
                        vm.subs = assgs;
                    }
                    else
                        vm.error = vm.error.length == 0 ? "Something wrong at our end. Please try again" : vm.error;
                })
        }

        init();
    }
})();