/**
 * This controller takes care of all the login related activities
 *
 * @author Keerthi Pendyala
 */
(function () {
    angular.module("CopyCatch")
        .controller("loginController", loginController);

    function loginController(userService, $location, $scope) {
        var vm = this;
        vm.login = login;
        vm.register = register;
        vm.checkProfessor = checkProfessor;
        vm.checkAdmin = checkAdmin;
        vm.checkStudent = checkStudent;
        vm.profRegister = profRegister;
        vm.logout = logout;


        /**
         * Logs user out of the application
         */
        function logout() {
            userService.logout()
                .then(
                    $location.url("/login"));
        }


        /**
         * init method is called every time the controller is loaded.
         */
        var init = function () {
            vm.student = true;
        };
        init();

        /**
         * logs in user with the right credentials, throws error message otherwise.
         * @param user object containing the username and password.
         */
        function login(user) {
            vm.error = "";
            vm.errormsg = ""
            if ($scope.loginForm.$valid) {
                userService
                    .login(user)
                    .then(function (usr) {
                        if (usr === "" || usr.username == null) {
                            vm.error = 'Account not found : Please check your credentials again'
                        }
                        else {
                            if (usr.role === 'student' || usr.role === 'professor')
                                $location.url('/course/' + usr.role + '/' + usr.userid);
                            else
                                $location.url('/adminProfView/' + usr.role + '/' + usr.userid);
                        }
                    });
            }
            else {
                $scope.loginForm.submitted = true;
                vm.errormsg = "Missing Fields";
            }
        }

        /**
         * navigates to register page
         */
        function register() {
            $location.url("/register");
        }

        /**
         * navigates to register page
         */
        function profRegister() {
            $location.url("/profRegister");
        }

        /**
         * toggle for professor login
         */
        function checkProfessor() {
            vm.student = false;
            vm.admin = false;
            vm.professor = true;
        }

        /**
         * toggle for admin login
         */
        function checkAdmin() {
            vm.student = false;
            vm.admin = true;
            vm.professor = false;
        }

        /**
         * toggle for student login
         */
        function checkStudent() {
            vm.student = true;
            vm.admin = false;
            vm.professor = false;
        }
    }
})();