/**
 * This controller takes care of all the register related activities
 */
(function () {
    angular.module("CopyCatch")
        .controller("registerController", registerController);

    function registerController(userService, $location, $scope) {
        var vm = this;
        vm.logout = logout;
        vm.register = register;
        vm.professorRegister = professorRegister;
        vm.goBack = goBack;
        vm.resetModal = resetModal;
        vm.resetForm = resetForm;


        /**
         * this method is used to send the user details for the registration, throws error message otherwise
         * @param user represents the user details required for registration
         */
        function createUser(user) {
            if (user.password === user.verifyPassword) {
                userService
                    .register(user)
                    .then(function (usr) {
                        if (usr !== 0) {
                            vm.error = null;
                            vm.noMatch = null;
                            vm.message = null;
                            vm.success = true;
                        }
                    });
            }
            else {
                vm.noMatch = "Passwords don't match";
            }
        }

        /**
         * navigates back to the previous page
         */
        function goBack() {
            history.go(-1);
        }

        /**
         * this method used to validate user details such as username, password and email format, throws error message otherwise
         * @param user object containing the user details to be updated.
         */
        function checkValidation(user) {
            var EMAIL_REGEXP = /^[_a-z0-9]+(\.[_a-z0-9]+)*@[a-z0-9-]+(\.[a-z0-9-]+)*(\.[a-z]{2,4})$/;
            if (user !== undefined)
                if (user.username === undefined || user.username.length < 3) {
                    vm.error = "Username should be atleast 3 characters"
                }
                else if (user.password === undefined || (user.password.length < 3 ||
                        user.password.length > 25))
                    vm.error = "Password length should be between 3 and 25";
                else if (!(EMAIL_REGEXP.test(user.email)))
                    vm.error = "Please enter valid email";
        }

        /**
         * this method is used to validate and send the user details for the registration, throws error message otherwise
         * @param user represents the user details required for registration
         */
        function register(user) {
            vm.message = "";
            vm.error = "";
            vm.noMatch = "";
            vm.success = false;
            checkValidation(user);
            if (vm.error.length === 0) {
                if ($scope.registerForm.$valid) {
                    user.role = 'student';
                    userService
                        .findUserByUsername(user.username)
                        .then(function (usr) {
                            if (usr) {
                                vm.message = "Username Taken";
                            }
                            else {
                                createUser(user);
                            }
                        });
                }
                else {
                    $scope.registerForm.submitted = true;
                    vm.error = "Missing fields!";
                }
            }
        }

        /**
        * this method is used to validate and send the professor registration request, throws error message otherwise
        * @param user represents the professor details for the registration request
        */
        function professorRegister(user) {
            vm.error = "";
            vm.profNoMatch = "";
            vm.profMessage = "";
            vm.profSuccess = false;
            checkValidation(user);
            if (vm.error.length === 0) {
                if ($scope.professorForm.$valid) {
                    user.role = 'professor';
                    userService
                        .findUserByUsername(user.username)
                        .then(function (usr) {
                            if (usr)
                                vm.profMessage = "Username Taken";
                            else
                                requestProfessorRegistration(user);
                        });
                }
                else {
                    $scope.professorForm.submitted = true;
                    vm.profError = "Missing fields!"
                }
            }
            vm.profError = vm.error, vm.error = "";
        }

        /**
         * this method is used to send the professor registration request, throws error message otherwise
         * @param user represents the professor details for the registration request
         */
        function requestProfessorRegistration(user) {
            if (user.password === user.verifyPassword) {
                userService
                    .requestProfessorRegistration(user)
                    .then(function (usr) {
                        if (usr !== 0) {
                            vm.error = null;
                            vm.noMatch = null;
                            vm.message = null;
                            vm.profSuccess = true;
                        }
                    });
            }
            else {
                vm.profNoMatch = "Passwords don't match";
            }
        }

        /**
         * this method is used to reset the user prof to use in the view
         */
        function resetModal() {
            $scope.prof = {};
        }

        /**
         * this method is used to reset the user model to use in the view
         */
        function resetForm() {
            $scope.user = {};
        }

        /**
         * logs out user from the session
         */
        function logout() {
            userService.logout()
                .then(function (usr) {
                    vm.error = 'Something went wrong at our end : Please try again'
                    $location.url('/course/' + usr.id);
                }, function (err) {
                    $location.url("/login");
                });
        }
    }
})();