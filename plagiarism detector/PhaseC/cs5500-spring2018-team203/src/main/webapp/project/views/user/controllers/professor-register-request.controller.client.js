/**
 * This controller takes care of all the professor register related activities
 */

(function () {
    angular.module("CopyCatch")
        .controller("profRegisterController", profRegisterController);

    function profRegisterController(userService, $location, $scope) {
        var vm = this;
        vm.professorRegister = professorRegister;
        vm.resetModal = resetModal;
        vm.goBack=goBack;

        /**
        * this method used to validate user details such as username, password and email format, throws error message otherwise
        * @param user object containing the user details to be updated.
        */
        function checkValidation(user){
            var EMAIL_REGEXP = /^[_a-z0-9]+(\.[_a-z0-9]+)*@[a-z0-9-]+(\.[a-z0-9-]+)*(\.[a-z]{2,4})$/;
            if(user !== undefined)
                if(user.username === undefined || user.username.length<3){
                    vm.profError = "Username should be atleast 3 characters"
                }
                else if(user.password === undefined || (user.password.length<3 ||
                                                        user.password.length>25))
                    vm.profError = "Password length should be between 3 and 25";
                else if(!(EMAIL_REGEXP.test(user.email)))
                    vm.profError = "Please enter valid email";
        }

        /**
        * this method is used to validate and send the professor registration request, throws error message otherwise
        * @param user represents the professor details for the registration request
        */
        function professorRegister(user) {
            vm.profError = "";
            vm.profnoMatch = "";
            vm.profMessage = "";
            vm.profSuccess = false;
            checkValidation(user);
            if(vm.profError.length === 0){
                user.role = 'professor';
                if ($scope.professorForm.$valid) {
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
        * this method is used to reset the user prof model
        */
        function resetModal() {
            $scope.prof = {};
        }

        /**
         * navigates back to the previous page
         */
        function goBack() {
            history.go(-1);
        }
    }
})();