/**
 * This controller takes care of all the violation summary related activities for a particular professor's courses
 */

(function () {
	angular.module("CopyCatch")
	.controller("profViolationsController", profViolationsController);

	function profViolationsController(submissionService, $routeParams, userService) {
		var vm = this;
		vm.userId = $routeParams['uid'];
		vm.goBack = goBack;

		vm.logout=logout;


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
			.findUserByIdAndRole(vm.userId, 'professor')
			.then(function (usr) {
				vm.userInfo = usr;
			});
			submissionService.getViolations(vm.userId)
			.then(function (violations) {
				vm.results = true;
				vm.violations = violations;
			});
		}
		init();

		/**
         * navigates back to the previous page
         */
		function goBack() {
			history.go(-1);
		}
	}
})();