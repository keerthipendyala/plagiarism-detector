/**
 * This controller helps with activities related to viewing courses
 */
(function () {
    angular.module("CopyCatch")
        .controller("assignmentController", assignmentController);

    function assignmentController(userService, assgService, courseService, submissionService, $location, $routeParams) {
        var vm = this;
        vm.logout = logout;
        vm.setUploadFor = setUploadFor;
        vm.submitAssg = submitAssg;
        vm.subHistory = subHistory;
        vm.userId = $routeParams['uid'];
        vm.courseid = $routeParams['cid'];
        vm.coursename = $routeParams['cname'];
        vm.goBack = goBack;
        vm.newAssg = {};

        /**
         * logs out user from the session
         */
        function logout() {
            userService.logout()
                .then(
                    $location.url("/login"));
        }

        /**
         * Set the upload flag to true, nullify all other messages
         * @param assg Uploading assignment
         */
        function setUploadFor(assg) {
            vm.msg = "";
            vm.err = "";
            vm.newAssg = {};
            vm.canUpload = true;
            vm.uploadingAssg = assg;
            vm.newAssg.assignmentid = assg.id;
        }

        /**
         * Navigates to the subhistory page
         * @param aid
         */
        function subHistory(aid) {
            $location.url("/" + vm.stuId + "/subhistory/" + vm.courseid + "/" + aid);
        }

        /**
         * Takes user to the previous page
         */
        function goBack() {
            $location.url("/course/" + vm.userInfo.role + "/" + vm.userId + "/" + vm.userInfo.studentid)
        }

        /**
         * Submits assignment after validating the git hub link
         */
        function submitAssg() {
            vm.msg = "";
            vm.err = "";
            vm.newAssg.studentid = vm.stuId;
            vm.newAssg.courseid = vm.courseid;
            vm.newAssg.studentname = vm.userInfo.username;
            var LINK_REGEX = /^(https:\/\/github.com)+([\/a-zA-Z0-9-.]*)+(.git)$/
            if (vm.newAssg.submissionname === undefined || vm.newAssg.submissionlink === undefined)
                vm.err = "Please provide Assignment submission name and upload link"
            else if (!LINK_REGEX.test(vm.newAssg.submissionlink))
                vm.err = "Please provide valid github link"
            else {
                submissionService
                    .submitAssg(vm.newAssg)
                    .then(function (resp) {
                        if (resp != 0)
                            vm.msg = "Successfully uploaded your assignment"
                        else
                            vm.err = "Assignment upload action failed. Please try again!!!"
                    })
            }
        }

        /**
         * init method is called every time the controller is loaded and initiates the user and course details.
         */
        function init() {
            vm.error = "";
            vm.canUpload = false;

            userService
                .findUserByIdAndRole(vm.userId, "student")
                .then(function (usr) {
                    vm.userInfo = usr;
                    vm.stuId = vm.userInfo.studentid;
                });
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