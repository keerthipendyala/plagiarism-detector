/**
 * This controller helps in editing and viewing the user's profile
 */
(function () {
    angular.module("CopyCatch")
        .controller("profileController", profile);

    /*
    * this function holds all the profile page related functionalities
    */
    function profile(userService, courseService, $location, $routeParams) {
        var vm = this;
        vm.logout = logout;
        vm.updateUser = updateUser;
        vm.validationAndUpdate = validationAndUpdate;
        vm.coursepage = coursepage;
        vm.adminpage = adminpage;
        vm.goBack = goBack;
        vm.userId = $routeParams['uid'];
        vm.userRole = $routeParams['role'];

        /**
         * logs out user from the session
         */
        function logout() {
            userService.logout()
                .then(
                    $location.url("/login"));
        }

        /**
         * this method updates the user fields, throws error message otherwise
         * @param user object containing the user details to be updated.
         */
        function updateUser(user) {
            if (user.dateofBirth != null)
                user.dateofBirth = user.dateofBirth.toISOString().slice(0, 10);
            if (user.role === 'student' && user.dateofgraduation != null)
                user.dateofgraduation = user.dateofgraduation.toISOString().slice(0, 10);
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
        };

        /**
         * this method used to validate user details such as username, password and email format, throws error message otherwise
         * @param user object containing the user details to be updated.
         */
        function checkValidation(user) {
            var EMAIL_REGEXP = /^[_a-z0-9]+(\.[_a-z0-9]+)*@[a-z0-9-]+(\.[a-z0-9-]+)*(\.[a-z]{2,4})$/;
            var pwdchk = (user.password === undefined || (user.password.length < 3 ||
                user.password.length > 25)) && vm.pwdLen !== user.password.length;

            if (user !== undefined)
                if (user.username === undefined || user.username.length < 3) {
                    vm.error = "Username should be atleast 3 characters"
                }
                else if (pwdchk)
                    vm.error = "Password length should be between 3 and 25";
                else if (!(EMAIL_REGEXP.test(user.email)))
                    vm.error = "Please enter valid email";
        }

        /**
         * this method updates the user fields by calling user service methods if the fields are valid
         * , throws error message otherwise
         * @param user object containing the user details to be updated.
         */
        function validationAndUpdate(user) {
            vm.error = "";
            vm.message = "";
            checkValidation(user)
            if (vm.error.length === 0)
                if (user.password === user.verifyPassword)
                    userService
                        .findUserByUsername(user.username)
                        .then(function (usr) {
                            vm.error = "";
                            if (usr === "" || usr.userid == user.userid) {
                                user.userid = vm.userId;
                                updateUser(user);
                            }
                            else
                                vm.error = "Username Taken Already.";
                        });
                else
                    vm.error = "Password doesn't match"
        }

        /**
         * navigates to course view page
         */
        function coursepage() {
            $location.url("/course/" + vm.userRole + "/" + vm.userId);
        }

        /**
         * navigates to admin view page that displays all users
         */
        function adminpage() {
            $location.url("/adminProfView/" + vm.userRole + "/" + vm.userId);
        }

        /**
         * navigates back to the previous page
         */
        function goBack() {
            history.go(-1);
        }

        /**
         * init method is called every time the controller is loaded and initiates the user object.
         */
        var init = function () {
            vm.error = "";
            vm.message = "";
            vm.noCourse = "";
            userService
                .findUserByIdAndRole(vm.userId, vm.userRole)
                .then(function (usr) {

                    vm.userInfo = usr;
                    vm.name = usr.username;
                    vm.userInfo.verifyPassword = usr.password
                    vm.pwdLen = usr.password.length
                    if (usr.dateofBirth !== null)
                        vm.userInfo.dateofBirth = new Date(usr.dateofBirth)
                    if (vm.userInfo.dateofgraduation !== null)
                        vm.userInfo.dateofgraduation = new Date(usr.dateofgraduation)
                })
        };
        init();
    }
})();