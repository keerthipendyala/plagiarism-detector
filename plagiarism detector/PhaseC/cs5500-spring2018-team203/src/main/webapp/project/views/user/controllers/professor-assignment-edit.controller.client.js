/**
 * This controller takes care of all the professor assignment upload related activities
 */

(function () {
    angular.module("CopyCatch")
        .controller("profAssignmentEditController", profAssignmentEditController);

    function profAssignmentEditController(userService, assgService, $routeParams, $route, $scope, $location) {

        var vm = this;
        vm.userId = $routeParams['uid'];
        vm.courseId = $routeParams['cid'];
        vm.assgId = $routeParams['aid'];
        vm.semesterId = $routeParams['sid'];

        vm.goBack = goBack;
        vm.editAssignment = editAssignment;
        vm.updateAssignment = updateAssignment;


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
                })

            assgService
                .getAllAssignments(vm.courseId)
                .then(function (assg) {
                    vm.assgInfo = assg;
                });
            assgService
                .getAssignmentById(vm.assgId)
                .then(function (assg) {
                    vm.assignment = assg;
                })

        };
        init();

        /**
         * Takes user to the previous page
         */
        function goBack() {
            $location.url("/" + vm.userId + "/prof-assg/" + vm.semesterId + "/" + vm.courseId);
        }

        /**
         * Updates the given assignment
         * @param assignment Assignment
         */
        function updateAssignment(assignment) {
            assignment.courseid = vm.courseId;
            assgService
                .updateAssignment(assignment)
                .then(function (usr) {
                    vm.userInfo = usr;
                });
            assgService
                .getAllAssignments(vm.courseId)
                .then(function (assg) {
                    vm.assgInfo = assg;
                });
            assgService
                .getAssignmentById(vm.assgId)
                .then(function (assg) {
                    vm.assignment = assg;
                    $route.reload();
                })
        }

        /**
         * Edits the given assignment
         * @param assgId id of assignment
         */
        function editAssignment(assgId) {
            $location.url("/" + vm.userId + "/prof-edit-assg/" + vm.semesterId + "/" + vm.courseId + "/" + assgId);
        }
    }
})();