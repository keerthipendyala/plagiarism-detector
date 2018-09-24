/**
 * This controller takes care of all the professor assignment upload related activities
 */

(function () {
    angular.module("CopyCatch")
        .controller("profAssignmentController", profAssignmentController);

    function profAssignmentController(userService, assgService, $routeParams, $location, $scope) {

        var vm = this;
        vm.userId = $routeParams['uid'];
        vm.courseId = $routeParams['cid'];
        vm.semesterId = $routeParams['sid'];
        vm.goBack = goBack;
        vm.addAssignment = addAssignment;
        vm.editAssignment = editAssignment;
        vm.deleteAssignment = deleteAssignment;
        vm.changeSortAsc = changeSortAsc;
        vm.changeSortDesc = changeSortDesc;
        vm.desc = true;
        vm.ddesc = true;


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
                })

        };
        init();

        /**
         * Takes user to the previous page
         */
        function goBack() {
            $location.url('/course/professor/' + vm.userInfo.userid);
        }

        /**
         * Navigates to add assignment page
         */
        function addAssignment() {
            $location.url("/" + vm.userId + "/prof-new-assg/" + vm.semesterId + "/" + vm.courseId);
        }

        /**
         * Navigates to edit assignment page
         */
        function editAssignment(assgId) {
            $location.url("/" + vm.userId + "/prof-edit-assg/" + vm.semesterId + "/" + vm.courseId + "/" + assgId);
        }

        /**
         * Navigates to delete assignment page
         */
        function deleteAssignment(assgId) {
            $location.url("/" + vm.userId + "/prof-delete-assg/" + vm.semesterId + "/" + vm.courseId + "/" + assgId);
        }

        /**
         * Sorts by the given type in ascending order
         * @param type on which sorting should happen
         */
        function changeSortAsc(type) {
            if (type === 'assignmentname') {
                vm.desc = false;
                vm.asc = true;
            } else {
                vm.ddesc = false;
                vm.dasc = true;
            }
            $scope.sortType = type;
            $scope.sortReverse = false;

        }

        /**
         * Sorts by the given type in descending order
         * @param type on which sorting should happen
         */
        function changeSortDesc(type) {
            if (type === 'assignmentname') {
                vm.desc = true;
                vm.asc = false;
            } else {
                vm.ddesc = true;
                vm.dasc = false;
            }
            $scope.sortType = type;
            $scope.sortReverse = true;
        }

    }
})();

//Reference: https://scotch.io/tutorials/sort-and-filter-a-table-using-angular