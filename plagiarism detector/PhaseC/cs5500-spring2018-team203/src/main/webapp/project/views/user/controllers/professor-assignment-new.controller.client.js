/**
 * This controller takes care of all the professor assignment upload related activities
 */

(function () {
    angular.module("CopyCatch")
        .controller("profAssignmentNewController", profAssignmentNewController);

    function profAssignmentNewController(userService, assgService, $routeParams, $route, $scope, $location) {

        var vm = this;
        vm.userId = $routeParams['uid'];
        vm.courseId = $routeParams['cid'];
        vm.semesterId = $routeParams['sid'];
        vm.goBack = goBack;
        vm.createAssignment = createAssignment;
        vm.resetAssignment = resetAssignment;

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
         * init method is called every time the controller is loaded and initiates the user details.
         */
        var init = function () {
            userService
                .findUserByIdAndRole(vm.userId, 'professor')
                .then(function (usr) {
                    vm.userInfo = usr;
                });

            assgService
                .getAllAssignments(vm.courseId)
                .then(function (assg) {
                    vm.assgInfo = assg;
                })

        };
        init();

        /**
         * Takes user to the previous page
         */
        function goBack() {
            history.go(-1);
        }

        /**
         * Creates the given assignment
         * @param assignment new assignment
         */
        function createAssignment(assignment) {
            assignment.courseid = vm.courseId;
            assgService
                .createAssignment(assignment)
                .then(function (usr) {
                    vm.userInfo = usr;
                    assgService
                        .getAllAssignments(vm.courseId)
                        .then(function (assg) {
                            vm.assgInfo = assg;
                        })
                    $route.reload();
                })
        }

        /**
         * Clears assignment data
         */
        function resetAssignment() {
            $scope.assignment = {};
        }
    }
})();