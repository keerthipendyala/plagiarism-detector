(function () {
    angular
        .module("CopyCatch")
        .config(configuration);

    function configuration($routeProvider, $httpProvider) {
        $httpProvider.defaults.headers.post['Content-Type'] = 'application/json;charset=utf-8';
        $httpProvider.defaults.headers.put['Content-Type'] = 'application/json;charset=utf-8';
        $routeProvider
            .when("/", {
                templateUrl: "views/user/templates/user-login.view.client.html",
                controller: 'loginController',
                controllerAs: 'model'
            }).when("/login", {
            templateUrl: "views/user/templates/user-login.view.client.html",
            controller: 'loginController',
            controllerAs: 'model'
        })
            .when("/course/:role/:uid", {
                templateUrl: "views/user/templates/student-course.view.client.html",
                controller: 'studentCourseController',
                controllerAs: 'model',
                resolve: {
                    loggedInUser: checkLogin

                }
            }).otherwise(
            {
                redirectTo: "/"
            })
            .when("/course/:role/:uid/:sid", {
                templateUrl: "views/user/templates/student-course.view.client.html",
                controller: 'studentCourseController',
                controllerAs: 'model',
                resolve: {
                    loggedInUser: checkLogin

                }
            }).otherwise(
            {
                redirectTo: '/login'
            })
            .when("/register", {
                templateUrl: "views/user/templates/user-register.view.client.html",
                controller: 'registerController',
                controllerAs: 'model'
            })
            .when("/profRegister", {
                templateUrl: "views/user/templates/professor-register-request.view.client.html",
                controller: 'profRegisterController',
                controllerAs: 'model'
            })
            .when("/profile/:role/:uid", {
                templateUrl: "views/user/templates/user-profile.view.client.html",
                controller: 'profileController',
                controllerAs: 'model',
                resolve: {
                    loggedInUser: checkLogin

                }
            }).otherwise(
            {
                redirectTo: '/login'
            })
            .when("/adminProfView/:role/:uid", {
                templateUrl: "views/user/templates/admin-professor-request.view.client.html",
                controller: 'adminProfController',
                controllerAs: 'model',
                resolve: {
                    loggedInUser: checkLogin,
                    isAdmin: isAdmin

                }
            }).otherwise(
            {
                redirectTo: '/login'
            })
            .when("/:uid/:cname/assgupload/:cid", {
                templateUrl: "views/user/templates/assg-upload.view.client.html",
                controller: 'assignmentController',
                controllerAs: 'model',
                resolve: {
                    loggedInUser: checkLogin

                }
            })
            .when("/:sid/subhistory/:cid/:aid", {
                templateUrl: "views/user/templates/subhistory.view.client.html",
                controller: 'subHistoryController',
                controllerAs: 'model',
                resolve: {
                    loggedInUser: checkLogin

                }
            }).otherwise(
            {
                redirectTo: '/login'
            })

            .when("/:uid/prof-assg/:sid/:cid", {
                templateUrl: "views/user/templates/professor-assignment-view.client.html",
                controller: 'profAssignmentController',
                controllerAs: 'model',
                resolve: {
                    loggedInUser: checkLogin,
                    isProfessor: isProfessor

                }
            }).otherwise(
            {
                redirectTo: '/login'
            })
            .when("/:uid/prof-new-assg/:sid/:cid", {
                templateUrl: "views/user/templates/professor-assignment-new.view.client.html",
                controller: 'profAssignmentNewController',
                controllerAs: 'model',
                resolve: {
                    loggedInUser: checkLogin,
                    isProfessor: isProfessor

                }
            })
            .when("/:uid/prof-edit-assg/:sid/:cid/:aid", {
                templateUrl: "views/user/templates/professor-assignment-edit.view.client.html",
                controller: 'profAssignmentEditController',
                controllerAs: 'model',
                resolve: {
                    loggedInUser: checkLogin,
                    isProfessor: isProfessor

                }
            }).otherwise(
            {
                redirectTo: '/login'
            })
            .when("/:uid/prof-delete-assg/:sid/:cid/:aid", {
                templateUrl: "views/user/templates/professor-assignment-delete.view.client.html",
                controller: 'profAssignmentDeleteController',
                controllerAs: 'model',
                resolve: {
                    loggedInUser: checkLogin,
                    isProfessor: isProfessor

                }
            }).otherwise(
            {
                redirectTo: '/login'
            })
            .when("/:uid/prof-submission/:sid/:cid/:aid", {
                templateUrl: "views/user/templates/professor-submission-view.client.html",
                controller: 'profSubmissionController',
                controllerAs: 'model',
                resolve: {
                    loggedInUser: checkLogin,
                    isProfessor: isProfessor

                }
            }).otherwise(
            {
                redirectTo: '/login'
            })
            .when("/:uid/prof-edit-course/:sid/:cid", {
                templateUrl: "views/user/templates/professor-course-edit.view.client.html",
                controller: 'profCourseEditController',
                controllerAs: 'model',
                resolve: {
                    loggedInUser: checkLogin,
                    isProfessor: isProfessor

                }
            }).otherwise(
            {
                redirectTo: '/login'
            })
            .when("/:uid/prof-create-course/:sid", {
                templateUrl: "views/user/templates/professor-course-create.view.client.html",
                controller: 'profCourseCreateController',
                controllerAs: 'model',
                resolve: {
                    loggedInUser: checkLogin,
                    isProfessor: isProfessor

                }
            }).otherwise(
            {
                redirectTo: '/login'
            })
            .when("/:uid/prof-delete-course/:sid/:cid", {
                templateUrl: "views/user/templates/professor-course-delete.view.client.html",
                controller: 'profCourseDeleteController',
                controllerAs: 'model',
                resolve: {
                    loggedInUser: checkLogin,
                    isProfessor: isProfessor

                }
            }).otherwise(
            {
                redirectTo: '/login'
            })
            .when("/:uid/prof-submission-violations", {
                templateUrl: "views/user/templates/professor-submission-violations.view.client.html",
                controller: 'profViolationsController',
                controllerAs: 'model',
                resolve: {
                    loggedInUser: checkLogin,
                    isProfessor: isProfessor

                }
            }).otherwise(
            {
                redirectTo: '/login'
            })
            .when("/:uid/student-enroll-course/:sid", {
                templateUrl: "views/user/templates/student-course-enroll.view.client.html",
                controller: 'studentCourseEnrollController',
                controllerAs: 'model',
                resolve: {
                    loggedInUser: checkLogin,
                    isStudent: isStudent

                }
            }).otherwise(
            {
                redirectTo: '/login'
            })
            .when("/getreport/:rid", {
                templateUrl: "views/user/templates/report.view.client.html",
                controller: 'reportController',
                controllerAs: 'model',
                resolve: {
                    loggedInUser: checkLogin
                }

            }).otherwise(
            {
                redirectTo: '/login'
            })


    }

    function checkLogin($q, userService, $location) {

        var deffered = $q.defer();

        userService.loggedIn()
            .then(function (user) {
                    if (user.userid == 0) {
                        $location.url('/');
                        deffered.reject();

                    }
                    if (user) {
                        deffered.resolve(user);
                    }
                }
            );
        return deffered.promise;

    }


    function isStudent($q, userService, $location) {

        var deffered = $q.defer();

        userService.isStudent()
            .then(
                function (user) {
                    if (user == 0) {
                        $location.url('/');
                        deffered.reject();

                    }
                    if (user) {
                        deffered.resolve(user);
                    }
                }
            );
        return deffered.promise;

    }


    function isProfessor($q, userService, $location) {

        var deffered = $q.defer();

        userService.isProfessor()
            .then(
                function (user) {
                    if (user == 0) {
                        $location.url('/');
                        deffered.reject();

                    }
                    if (user) {
                        deffered.resolve(user);
                    }
                }
            );
        return deffered.promise;

    }

    function isAdmin($q, userService, $location) {

        var deffered = $q.defer();

        userService.isAdmin()
            .then(
                function (user) {
                    if (user == 0) {
                        $location.url('/');
                        deffered.reject();

                    }
                    if (user) {
                        deffered.resolve(user);
                    }
                }
            );
        return deffered.promise;

    }

})();