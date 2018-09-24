/**
 * This controller takes care of all the professor assignment upload related activities
 */

(function () {
    angular.module("CopyCatch")
        .controller("profAssignmentDeleteController", profAssignmentDeleteController);

    function profAssignmentDeleteController(userService, assgService, $timeout, $routeParams, $route, $scope, $location) {

        var vm = this;
        vm.userId = $routeParams['uid'];
        vm.courseId = $routeParams['cid'];
        vm.assgId = $routeParams['aid'];
        vm.semesterId = $routeParams['sid'];
        vm.goBack = goBack;
        vm.deleteAssignment = deleteAssignment;


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
         * init method is called every time the controller is loaded and initiates the user and assignment details.
         */
        var init = function () {
            userService
                .findUserByIdAndRole(vm.userId, 'professor')
                .then(function (usr) {
                    vm.userInfo = usr;
                })

            assgService
                .getAllAssignments(vm.courseId)
                .then(function (assg) {
                    vm.assgInfo = assg;
                    for (var i = 0; i < assg.length; i++) {
                        if (assg[i].id == vm.assgId) {
                            vm.assignment = assg[i];
                        }
                    }
                })

        }
        init();

        /**
         * Takes user to the previous page
         */
        function goBack() {
            history.go(-1);
        }


        /**
         * Delete the given assignment
         */
        function deleteAssignment() {
            assgService
                .deleteAssignment(vm.assgId)
                .then(function (usr) {
                    $timeout(function () {
                        $location.url("/" + vm.userId + "/prof-assg/" + vm.semesterId + "/" + vm.courseId);
                    }, 3000);
                })
        }

        /**
         * template for adding a shredder effect on delete action
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