/**
 * This controller takes care of all the professor's course edit related activities
 */

(function () {
    angular.module("CopyCatch")
        .controller("profCourseEditController", profCourseEditController);

    function profCourseEditController(userService, courseService, $routeParams, $route, $scope, $location) {

        var vm = this;
        vm.userId = $routeParams['uid'];
        vm.semesterId = $routeParams['sid'];
        vm.courseId = $routeParams['cid'];
        vm.goBack = goBack;
        vm.editCourse = editCourse;
        vm.updateCourse = updateCourse;
        vm.userInfo = "";


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
         * init method is called every time the controller is loaded and initiates the user and course details.
         */
        var init = function () {
            userService
                .findUserByIdAndRole(vm.userId, 'professor')
                .then(function (usr) {
                    vm.userInfo = usr;
                    courseService
                        .getAllCoursesForProfessor(vm.semesterId, usr.professorid)
                        .then(function (uCourses) {
                            vm.userCourses = uCourses;
                        });
                    courseService
                        .getCourseForProfessorById(vm.semesterId, vm.courseId, usr.professorid)
                        .then(function (c) {
                            vm.profCourse = c;
                        })
                });


        };
        init();

        /**
         * navigates to the course page for the professor
         */
        function goBack() {
            $location.url('/course/professor' + '/' + vm.userInfo.userid + "/" + vm.semesterId);
        }

        /**
         * this method is used to initaite the update for the particular course details
         * @param course reprenst the course object which holds all the updated details to initiate the update action
         */
        function updateCourse(course) {
            if (course.threshold == null)
                vm.error = "Invalid Threshold!";
            else {
                courseService
                    .updateCourse(course)
                    .then(function (cr) {
                    });
                vm.error = "";
                $route.reload();
            }
        }

        /**
         * navigates to the course edit page for the professor
         */
        function editCourse(courseId) {
            $location.url("/" + vm.userId + "/prof-edit-course/" + vm.semesterId
                + "/" + courseId);
        }
    }
})();