/**
 * This controller takes care of all the professor course creating related activities
 */

(function () {
    angular.module("CopyCatch")
        .controller("profCourseCreateController", profCourseCreateController);

    function profCourseCreateController(userService, courseService, $routeParams, $route, $scope, $location) {

        var vm = this;
        vm.userId = $routeParams['uid'];
        vm.semesterId = $routeParams['sid'];
        vm.goBack = goBack;
        vm.createCourse = createCourse;
        vm.resetCourse = resetCourse;

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
         * init method is called every time the controller is loaded and initiates the user, and course details.
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
                });
        }
        init();

        /**
         * navigates to the course page for the professor
         */
        function goBack() {
            $location.url('/course/professor' + '/' + vm.userInfo.userid + "/" + vm.semesterId);
        }

        /**
         * this method initiates the course adding action for a particular professor under particular semester
         */
        function createCourse(course) {
            course.coursename = course.coursefirstname + "-" + course.courselastname;
            course.semid = vm.semesterId;
            course.profid = vm.userInfo.professorid;
            courseService
                .createCourse(course)
                .then(function (cr) {
                    courseService
                        .getAllCoursesForProfessor(vm.semesterId, vm.userInfo.professorid)
                        .then(function (uCourses) {
                            vm.userCourses = uCourses;
                        });
                    $route.reload();
                });
        }

        /**
         * this method is used to reset the user prof course model
         */
        function resetCourse() {
            $scope.profCourse = {};
        }
    }
})();