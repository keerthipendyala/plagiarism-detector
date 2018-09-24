/**
 * This controller takes care of all the professor course delete related activities
 */

(function () {
    angular.module("CopyCatch")
        .controller("profCourseDeleteController", profCourseDeleteController);

    function profCourseDeleteController(userService, courseService, $routeParams, $route, $timeout, $location) {

        var vm = this;
        vm.userId = $routeParams['uid'];
        vm.semesterId = $routeParams['sid'];
        vm.courseId = $routeParams['cid'];
        vm.goBack = goBack;
        vm.deleteCourse = deleteCourse;


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
         * this method is used to initaite the delete action for the particular course
         */
        function deleteCourse() {
            courseService
                .deleteCourse(vm.courseId)
                .then(function (cr) {
                    $timeout(function () {
                        $location.url('/course/professor' + '/' + vm.userInfo.userid + "/" + vm.semesterId);
                    }, 3000);
                })
        }

        /**
         * this method initiates the styling part for delete action
         */
        $(document).ready(function () {

            $('.shred-me').click(function () {
                $('.shredded-paper').addClass('animate-shredded-paper');
                $('.shredded-paper > .content').addClass('animate-content');
                $('.shredder-holder').addClass('shredded-holder-animate');

                $('.shredded-paper .part-1, .shredded-paper .part-2, .shredded-paper .part-4, .shredded-paper .part-6, .shredded-paper .part-8, .shredded-paper .part-10').addClass('shredded-paper-p-animate');

                $('.shredded-paper .part-3, .shredded-paper .part-5, .shredded-paper .part-7, .shredded-paper .part-9').addClass('shredded-paper-q-animate');

                setTimeout(function () {

                    $('.shredded-paper').css({'top': '-300px'});

                    $('.shredded-paper').removeClass('animate-shredded-paper');
                    $('.shredded-paper > .content').removeClass('animate-content');
                    $('.shredder-holder').removeClass('shredded-holder-animate');

                    $('.shredded-paper .part-1, .shredded-paper .part-2, .shredded-paper .part-4, .shredded-paper .part-6, .shredded-paper .part-8, .shredded-paper .part-10').removeClass('shredded-paper-p-animate');

                    $('.shredded-paper .part-3, .shredded-paper .part-5, .shredded-paper .part-7, .shredded-paper .part-9').removeClass('shredded-paper-q-animate');

                }, 4000);
            });
        });
    }
})();


//Reference : https://www.inserthtml.com/2013/06/css-shredder/