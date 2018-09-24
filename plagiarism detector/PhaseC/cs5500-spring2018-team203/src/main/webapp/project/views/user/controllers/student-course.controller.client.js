/**
 * This controller helps with all activities related to courses of a particular user
 */
(function () {
    angular.module("CopyCatch")
        .controller("studentCourseController", studentCourse);

    function studentCourse(userService, courseService, semesterService, $location, $routeParams, $scope) {
        var vm = this;
        vm.logout = logout;
        vm.assg_page = assg_page;
        vm.goBack = goBack;
        vm.userId = $routeParams['uid'];
        vm.userRole = $routeParams['role'];
        vm.semesterId = $routeParams['sid'];
        vm.editCourse = editCourse;
        vm.changeSelectedSemester = changeSelectedSemester;
        vm.createCourse = createCourse;
        vm.deleteCourse = deleteCourse;
        vm.changeSortType = changeSortType;
        vm.enrollCourse = enrollCourse;
        vm.viewViolations = viewViolations;
        vm.selectedSemester = "";
        vm.userInfo = "";
        $scope.sortType = 'departmentname';

        /**
         * logs out user from the session
         */
        function logout() {
            userService.logout()
                .then(
                    $location.url("/login"));
        }

        /**
         * navigates to the course page for the user
         */
        function assg_page(course) {
            if (vm.userRole === 'professor')
                $location.url("/" + vm.userId + "/prof-assg/" + vm.selectedSemester.semesterid
                    + "/" + course.courseid);
            else if (vm.userRole === 'student')
                $location.url("/" + vm.userId + "/" + course.coursename + "/assgupload/" + course.courseid);
        }

        /**
         * navigates back to the previous page
         */
        function goBack() {
            history.go(-1);
        }

        /**
         * init method is called every time the controller is loaded and initiates the user, semester details, and course details.
         */
        var init = function () {
            semesterService
                .getAllSemesters()
                .then(function (sms) {
                    vm.semesters = sms;
                    if (vm.semesterId == null)
                        vm.selectedSemester = sms[0];
                    else {
                        for (var i = 0; i < sms.length; i++)
                            if (sms[i].semesterid == vm.semesterId)
                                vm.selectedSemester = sms[i];
                    }
                    userService
                        .findUserByIdAndRole(vm.userId, vm.userRole)
                        .then(function (usr) {
                            vm.userInfo = usr;
                            if (usr.role === 'professor') {
                                courseService
                                    .getAllCoursesForProfessor(vm.selectedSemester.semesterid,
                                        vm.userInfo.professorid)
                                    .then(function (uCourses) {
                                        vm.userCourses = uCourses;
                                    })
                            }
                            else {
                                courseService
                                    .getCoursesForStudent(vm.userInfo.studentid,
                                        vm.selectedSemester.semesterid)
                                    .then(function (uCourses) {
                                        vm.userCourses = uCourses;
                                    })
                            }
                        });
                });
        };
        init();

        /**
         * navigates to course edit page for the professor for a particular semester
         * courseId represents the unique id for a course
         */
        function editCourse(courseId) {
            $location.url("/" + vm.userId + "/prof-edit-course/" + vm.selectedSemester.semesterid
                + "/" + courseId);
        }

        /**
         * navigates to course create page for the professor for a particular semester
         * courseId reresents the unique id for a course
         */
        function createCourse() {
            $location.url("/" + vm.userId + "/prof-create-course/" + vm.selectedSemester.semesterid);
        }

        /**
         * navigates to course enroll page for the professor for a particular semester
         */
        function enrollCourse() {
            $location.url("/" + vm.userId + "/student-enroll-course/" + vm.selectedSemester.semesterid);
        }

        /**
         * this method retrieves all enrolled course list for a particular user for the provided semester
         * sem representes the semester that has been selected from view page
         */
        function changeSelectedSemester(sem) {
            vm.selectedSemester = sem;
            if (vm.userInfo.role == 'professor') {
                courseService
                    .getAllCoursesForProfessor(vm.selectedSemester.semesterid,
                        vm.userInfo.professorid)
                    .then(function (uCourses) {
                        vm.userCourses = uCourses;
                    })
            }
            else
                courseService
                    .getCoursesForStudent(vm.userInfo.studentid,
                        vm.selectedSemester.semesterid)
                    .then(function (uCourses) {
                        vm.userCourses = uCourses;
                    })

        }

        /**
         * this methods changes the view of courses according to the selected sort type
         * type represents the type of sorting that needs to be performed
         */
        function changeSortType(type) {
            $scope.sortType = type;
        }

        /**
         * this methods deletes the particular from course list of the user
         * courseId represents the unique id for a course
         */
        function deleteCourse(courseId) {
            $location.url("/" + vm.userId + "/prof-delete-course/" + vm.selectedSemester.semesterid
                + "/" + courseId);
        }

        /**
         * this methods is used to view the violation summary for the particular professor course list
         */
        function viewViolations() {
            $location.url("/" + vm.userId + "/prof-submission-violations");
        }
    }
})();
