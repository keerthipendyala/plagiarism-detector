/**
 * This controller helps with activities related to viewing courses
 */
(function () {
    angular.module("CopyCatch")
        .controller("assgUploadController", assgUpload);

    function assgUpload(userService, $location, $routeParams) {
        var vm = this;
        vm.logout = logout;
        vm.upload = upload;
        vm.userId = $routeParams['uid'];
        vm.courseid = $routeParams['cid'];

        /**
         * logs out user from the session
         */
        function logout() {
            userService.logout()
                .then(
                    $location.url("/login"));
        }

        /**
         * Upload assignment
         * @param assg Given assignment
         */
        function upload(assg) {

        }

        /**
         * init method is called every time the controller is loaded and initiates the user details.
         */
        function init() {
            vm.error = "";
            courseService
                .getCourse(vm.courseid)
                .then(function (course) {
                    if (course !== undefined)
                        vm.course = course;
                    else
                        vm.error = "Something wrong at our end. Please try again";
                })

            assgService
                .getAllAssg(vm.courseid)
                .then(function (assgs) {
                    if (assgs !== undefined) {
                        vm.assgs = assgs;

                    }
                    else
                        vm.error = vm.error.length == 0 ? "Something wrong at our end. Please try again" : vm.error;
                })
        }

        init();
    }
})();