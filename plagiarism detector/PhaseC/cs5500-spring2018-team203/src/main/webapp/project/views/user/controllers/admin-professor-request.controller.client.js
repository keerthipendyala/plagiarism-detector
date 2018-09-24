/**
 * This controller takes care of all the admin professor register related activities
 */

(function () {
    angular.module("CopyCatch")
        .controller("adminProfController", adminProfController);

    function adminProfController(userService, $routeParams, $route, $location) {

        var vm = this;
        vm.rejectRequest = rejectRequest;
        vm.acceptRequest = acceptRequest;
        vm.goBack = goBack;
        vm.userId = $routeParams['uid'];
        vm.updateRole = updateRole;
        vm.deleteUser = deleteUser;
        vm.editUser = editUser;
        vm.updateUsr = updateUsr;
        vm.registerusr = registerusr;
        vm.logout = logout;
        vm.roles = ["student", "professor", "admin"];

        /**
         * Updates the user role of given user
         * @param usr User
         */
        function updateRole(usr) {
            vm.message = "";
            vm.error = "";
            if (usr.newrole === undefined)
                vm.error = "Please select any role from the dropdown list."
            else if (usr.newrole === usr.role)
                vm.error = "The User has already been in the requested role !!!";
            else
                userService
                    .changeRole(usr)
                    .then(function (resp) {
                        if (resp === 1)
                            vm.message = "The User role has been updated successfully.";
                        else if (resp === -1)
                            vm.error = "The User has already been in the requested role !!!";
                        else
                            vm.error = "Something went wrong. Please try again !!!"
                    });
        }

        /**
         * Deletes the given user
         * @param usr User
         */
        function deleteUser(usr) {
            var conf = confirm("Are you sure you want to delete this user?");
            vm.message = "";
            vm.error = "";
            if (conf == true) {
                userService
                    .deleteUser(usr)
                    .then(function (resp) {
                        if (resp === 1)
                            vm.message = "User has been deleted successfully."
                        else
                            vm.error = "User delete actionis failed. Please try again later !!!"
                    })
            }
        }

        /**
         * Edits the details of given user
         * @param usr User
         */
        function editUser(usr) {
            vm.editusr = "";
            userService
                .findUserByIdAndRole(usr.userid, usr.role)
                .then(function (usr) {
                    vm.editusr = usr;
                });
        }

        /**
         * Updates the profile of the given user
         * @param user User
         */
        function updateUsr(user) {
            vm.error = "";
            vm.message = "";
            userService
                .updateUser(user)
                .then(function (usr) {
                    if (usr != 0) {
                        vm.message = "Account is updated successfully";
                    }
                    else {
                        vm.error = "Account updation failed. Please try again !!!";
                    }
                });
        }

        /**
         * Validates username , password and email for given user
         * @param user User
         */
        function checkValidation(user) {
            var EMAIL_REGEXP = /^[_a-z0-9]+(\.[_a-z0-9]+)*@[a-z0-9-]+(\.[a-z0-9-]+)*(\.[a-z]{2,4})$/;
            if (user === undefined)
                vm.error = "Please enter User details"
            else {
                if (user.username === undefined || user.username.length < 3) {
                    vm.error = "Username should be atleast 3 characters"
                }
                else if (user.password === undefined || (user.password.length < 3 ||
                        user.password.length > 25))
                    vm.error = "Password length should be between 3 and 25";
                else if (!(EMAIL_REGEXP.test(user.email)))
                    vm.error = "Please enter valid email";
                else if (user.role === undefined)
                    vm.error = "Please select the User role"
            }
        }

        /**
         * Creates a new user
         * @param user User
         */
        function registerusr(user) {
            vm.error = ""
            checkValidation(user);
            if (vm.error.length === 0) {
                userService
                    .findUserByUsername(user.username)
                    .then(function (usr) {
                        if (usr)
                            vm.error = "Username Taken";
                        else
                            userService
                                .register(user)
                                .then(function (usr) {
                                    if (usr !== 0) {
                                        vm.message = "User created successfully"
                                    }
                                });
                    });
            }
        }

        /**
         * init method is called every time the controller is loaded and initiates the user details.
         */
        var init = function () {
            vm.error = "";
            vm.message = "";

            userService
                .findUserByIdAndRole($routeParams['uid'], "admin")
                .then(function (usr) {
                    vm.userInfo = usr;
                });

            userService
                .findAllProfessorRequests()
                .then(function (profs) {
                    if (profs.length === 0) {
                        vm.rmessage = "No requests!"
                    }
                    else {
                        vm.professor_info = profs;
                    }
                });
            userService
                .getAllProfessors()
                .then(function (professors) {
                    if (professors.length === 0) {
                        vm.pmessage = "No professors registered!"
                    }
                    else {
                        vm.professor_all = professors;
                    }
                });
            userService
                .getAllStudents()
                .then(function (students) {
                    if (students.length === 0) {
                        vm.smessage = "No students registered!"
                    }
                    else {
                        vm.student_all = students;
                    }
                });

        }
        init();

        /**
         * Takes user to the previous page
         */
        function goBack() {
            history.go(-1);
        }

        /**
         * Accepts professor registration request
         * @param profId Id of given professor
         */
        function acceptRequest(profId) {
            userService
                .acceptRequest(profId)
                .then(function (usr) {
                    $route.reload();
                });
        }

        /**
         * Rejects professor registration request
         * @param profId Id of the given professor
         */
        function rejectRequest(profId) {
            userService
                .rejectRequest(profId)
                .then(function (usr) {
                    $route.reload();
                });
        }

        /**
         * logs out user from the session
         */
        function logout() {
            userService.logout()
                .then(
                    $location.url("/login"));
        }

    }
})();