/**
 * This controller takes care of all the course enrollment related activities
 */

(function () {
    angular.module("CopyCatch")
        .controller("studentCourseEnrollController", studentCourseEnrollController);

    function studentCourseEnrollController(userService, courseService, $routeParams, $route, $scope, $location) {

        var vm = this;
        vm.userId = $routeParams['uid'];
        vm.semesterId = $routeParams['sid'];
        vm.goBack = goBack;
        vm.enrollCourse = enrollCourse;

        vm.logout = logout;

        /**
         * logs out user from the session
         */
        function logout() {
            userService.logout()
                .then(
                    $location.url("/login"));
        }

        /**
         * init method is called every time the controller is loaded and initiates the user object.
         */
        var init = function () {
            userService
                .findUserByIdAndRole(vm.userId, 'student')
                .then(function (usr) {
                    vm.userInfo = usr;
                    courseService
                        .getUnregisteredCourses(usr.studentid, vm.semesterId)
                        .then(function (crs) {
                            vm.unregistered = crs;
                        });
                });

        };
        init();

        /**
         * navigates back to the course view page
         */
        function goBack() {
            $location.url('/course/student' + '/' + vm.userInfo.userid + "/" + vm.semesterId);
        }

        /**
         * this method is used to enroll the user under a particular course
         * @param course represents the course object that has to be added for the user's course list.
         */
        function enrollCourse(course) {
            courseService
                .enroll(vm.userInfo.studentid, course.courseid)
                .then(function (crs) {
                    $location.url('/course/student' + '/' + vm.userInfo.userid + "/" + vm.semesterId);
                });
        }
    }
})();