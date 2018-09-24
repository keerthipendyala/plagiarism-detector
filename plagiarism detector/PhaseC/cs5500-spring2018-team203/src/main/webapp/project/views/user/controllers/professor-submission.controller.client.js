/**
 * This controller takes care of all the submission compare, and report related activities
 */

(function () {
    angular.module("CopyCatch")
        .controller("profSubmissionController", profSubmissionController);

    function profSubmissionController(userService, semesterService, assgService,
                                      submissionService, courseService, $routeParams, $scope, $timeout, $location) {
        var vm = this;
        vm.userId = $routeParams['uid'];
        vm.courseId = $routeParams['cid'];
        vm.semesterId = $routeParams['sid'];
        vm.assgId = $routeParams['aid'];
        vm.compare = compare;
        vm.compareAll = compareAll;
        vm.checkChange = checkChange;
        vm.selectedSubs = [];
        vm.selectedSemesters = [];
        vm.prevSemesters = [];
        vm.back = back;
        vm.goBack = goBack;
        vm.displayResults = displayResults;
        vm.progress = 0;
        vm.makeProgress = makeProgress;
        vm.pushSemester = pushSemester;
        vm.popSemester = popSemester;
        vm.logout = logout;
        vm.pushSemester = pushSemester;
        vm.popSemester = popSemester;
        vm.getAllSemestersCourses = getAllSemestersCourses;
        vm.selectedSemestersId = [];
        vm.checkSectionsChange = checkSectionsChange;
        vm.sectionCheck = false;
        vm.allSubmissions = [];
        vm.getAllSelectedSubs = getAllSelectedSubs;
        vm.sendemail = sendemail;
        vm.emailmessage = "";


        /**
         * Sends email of report to the given email Ids
         * @param email1 First Email Id
         * @param email2 Second Email Id
         * @param reportid Id of report to be sent
         */
        function sendemail(email1, email2, reportid) {

            userService.sendEmail(email1, email2, reportid).then(
                function (status) {
                    if (status == 1) {
                        vm.emailmessage = "Email sent successfully";
                    }
                    else {
                        vm.emailmessage = "Error in sending email";
                    }

                }
            )

        }


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
                    courseService
                        .getCourseForProfessorById(vm.semesterId, vm.courseId, usr.professorid)
                        .then(function (c) {
                            vm.threshold = c.threshold;
                            vm.courseNo = c.courseno;
                        })
                });

            semesterService
                .getAllSemesters()
                .then(function (sems) {
                    for (var i = 0; i < sems.length; i++) {
                        if (sems[i].semesterid != vm.semesterId) {
                            vm.prevSemesters.push(sems[i]);
                        }
                    }

                });

            submissionService
                .getAllSubmissionsByAllStudents(vm.courseId, vm.assgId)
                .then(function (submissions) {
                    vm.submissionInfo = submissions;
                });

            vm.message = "Please check your email for a detailed report.."
        }
        init();

        /**
         * Takes user to the previous page
         */
        function goBack() {
            history.go(-1);
        }

        function compare(selections) {
            vm.timeout = 6500;
            if (selections)
                vm.timeout = 12000;

            submissionService.compare(vm.selectedSubs)
                .then(function (res) {
                    if (res === 1) {
                        $timeout(function () {
                            $scope.compareResults = true;
                        }, vm.timeout);
                    }
                });
        }


        /**
         * Resets all message to empty on pressing back button
         */
        function back() {
            vm.results = false;
            vm.com = false;
            vm.comAll = false;
            $scope.compareResults = false;
        }

        /**
         * Adds / Removes submissions to compare based on clicking the checkbox in UI
         * @param checkValue value of checkbox , true if checked false otherwise
         * @param submission Given submission
         */
        function checkChange(checkValue, submission) {
            if (checkValue === true)
                vm.selectedSubs.push(submission);
            else
                vm.selectedSubs.pop(submission);

        }

        /**
         * Compares all the submission displayed on the page
         * @param selections All submission selected exclusively
         */
        function compareAll(selections) {
            vm.timeout = 6500;
            if (selections)
                vm.timeout = 12000;
            vm.allSubmissions.push.apply(vm.allSubmissions, vm.submissionInfo);
            for (var i = 0; i < vm.selectedSubs.length; i++) {
                if (vm.submissionInfo.indexOf(vm.selectedSubs[i]) === -1)
                    vm.allSubmissions.push(vm.selectedSubs[i])
            }
            submissionService.compare(vm.allSubmissions)
                .then(function (res) {
                    if (res === 1) {
                        $timeout(function () {
                            $scope.compareResults = true;
                        }, vm.timeout);
                    }
                });
        }

        /**
         * Displays results after comparison
         */
        function displayResults() {
            if (vm.com)
                vm.allsubsCom = vm.selectedSubs;
            else if (vm.comAll)
                vm.allsubsCom = vm.allSubmissions;

            submissionService
                .getScores(vm.allsubsCom)
                .then(function (subScores) {
                    vm.results = true;
                    vm.subScores = subScores;
                });

        }

        /**
         * Displays the progress bar and calls compare functions
         * @param compare true if one or more comparisons are selected , false if all comparisons are selected
         */
        function makeProgress(compare) {
            vm.startProgress = true;
            if (compare)
                vm.com = true;
            else
                vm.comAll = true;
            if (vm.selectedSemesters.length === 0 && !vm.sectionCheck) {
                vm.timer = 300;
                if (compare)
                    vm.compare(false);
                else
                    vm.compareAll(false);
            }
            else {
                vm.timer = 1000;
                getAllSemestersCourses(compare);
            }
            var current_progress = 0;
            var interval = setInterval(function () {
                current_progress += 5;
                $("#dynamic")
                    .css("width", current_progress + "%")
                    .attr("aria-valuenow", current_progress)
                    .text(current_progress + "%");
                if (current_progress >= 100) {
                    clearInterval(interval);
                }
            }, vm.timer);
        }

        /**
         * Adds Semester to list of semester submissions to compare
         * @param s Semester chosen
         */
        function pushSemester(s) {
            var index = vm.prevSemesters.indexOf(s);
            if (index >= 0) {
                vm.selectedSemesters.push(s);
                vm.prevSemesters.splice(index, 1);
            }
        }

        /**
         * Removes Semester from list of semester submissions to compare
         * @param s Semester chosen
         */
        function popSemester(s) {
            var index = vm.selectedSemesters.indexOf(s);
            vm.selectedSemesters.splice(index, 1);
            vm.prevSemesters.push(s);

        }

        /**
         * Checks Toggle for cross section comparison
         * @param value true enables cross-section , false disables it
         */
        function checkSectionsChange(value) {
            vm.sectionCheck = value;
        }

        /**
         * Gets all the courses whose submissions should be compared
         * including cross section and cross semester if selected.
         * @param compare true if one or more comparisons are selected , false if all comparisons are selected
         */
        function getAllSemestersCourses(compare) {
            for (var i = 0; i < vm.selectedSemesters.length; i++) {
                vm.selectedSemestersId.push(vm.selectedSemesters[i].semesterid)
            }
            submissionService
                .getAllCompareCourses(vm.semesterId, vm.courseId, vm.courseNo, vm.sectionCheck, vm.selectedSemestersId)
                .then(function (courses) {
                    vm.selectCourses = courses;
                    for (var i = 0; i < vm.selectCourses.length; i++) {
                        vm.getAllSelectedSubs(vm.selectCourses[i]);
                        if (i == vm.selectCourses.length - 1) {
                            $timeout(function () {
                                if (compare)
                                    vm.compare(true);
                                else
                                    vm.compareAll(true)
                            }, 8000);
                        }
                    }

                });
        }

        /**
         * Gets all the submissions for the given course which should be compared
         * including cross section and cross semester if selected.
         * @param selectCourse chosen course
         */
        function getAllSelectedSubs(selectCourse) {
            assgService
                .getAllAssignments(selectCourse)
                .then(function (assignments) {
                    vm.courseAssignments = assignments;
                    for (var j = 0; j < vm.courseAssignments.length; j++)
                        submissionService
                            .getAllSubmissionsByAllStudents(selectCourse, vm.courseAssignments[j].id)
                            .then(function (submissions) {
                                for (var k = 0; k < submissions.length; k++) {
                                    vm.selectedSubs.push(submissions[k])
                                }
                            });
                });
        }

    }
})();