/**
 * This services sends https requests to the server for all user related manipulations
 */

(function () {
    angular.module("CopyCatch")
        .factory('userService', userService);

    function userService($http) {
        var api = {
            "login": login,
            "logout": logout,
            "findUserById": findUserById,
            "findUserByUsername": findUserByUsername,
            "register": register,
            "requestProfessorRegistration": requestProfessorRegistration,
            "findUserByIdAndRole": findUserByIdAndRole,
            "findAllProfessorRequests": findAllProfessorRequests,
            "acceptRequest": acceptRequest,
            "rejectRequest": rejectRequest,
            "updateUser": updateUser,
            "getAllProfessors": getAllProfessors,
            "getAllStudents": getAllStudents,
            "changeRole": changeRole,
            "deleteUser": deleteUser,
            "loggedIn": loggedIn,
            "isStudent": isStudent,
            "isProfessor": isProfessor,
            "isAdmin": isAdmin,
            "sendEmail": sendEmail

        };
        return api;


        /**
         * Send reports to the given email Ids
         * @param email1 first email Id
         * @param email2 second email Id
         * @param reportid id of the report that should be sent
         *
         */
        function sendEmail(email1, email2, reportid) {
            return $http.get("/api/sendemail/" + email1 + "/" + email2 + "/" + reportid).then(
                function (response) {
                    return response.data;
                });

        }


        /**
         * this method is to check the users credentials for logging in
         * @param user object containing the username and password.
         */
        function login(user) {
            return $http.post("/api/login", user)
                .then(function (response) {
                    return response.data;
                });
        }

        /**
         * this method is to end the user session by logging out
         */
        function logout() {
            return $http.get("/api/logout")
                .then(function (response) {
                    return response.data;
                });
        }

        /**
         * this method retrieves the user object that matches the passed userId
         * @param userId represents the unique id of the user
         */
        function findUserById(userId) {
            return $http.get("/api/findUser/" + userId)
                .then(function (response) {
                    return response.data;
                });
        }

        /**
         * this method creates new user record in the database
         * @param user represents the user object with required field for registration
         */
        function register(user) {
            return $http.post("/api/register", user)
                .then(function (response) {
                    return response.data;
                });
        }

        /**
         * this method retrieves the user object that matches the passed username
         * @param username represents the unique username for a particular user
         */
        function findUserByUsername(username) {
            return $http.get("/api/findUsername/" + username)
                .then(function (response) {
                    return response.data;
                });
        }

        /**
         * this method sends the details required for registering an user as a professor
         * @param user represents the user object
         */
        function requestProfessorRegistration(user) {
            return $http.post("/api/requestProfessorRegistration", user)
                .then(function (response) {
                    return response.data;
                });
        }

        /**
         * this method retrieves the user that matches the passed userId and role
         * @param userId represents the unique id of the user
         * @param role represents the role of an user
         */
        function findUserByIdAndRole(userId, role) {
            return $http.get("/api/findUserByIdAndRole/" + userId + "/" + role)
                .then(function (response) {
                    return response.data;
                });
        }

        /**
         * this method update an user details in the database
         * @param user represents the user object
         */
        function updateUser(user) {
            if (user.role === 'student') {
                return $http.post("/api/updateStudent/" + user.userid, user)
                    .then(function (response) {
                        return response.data;
                    });
            }
            else if (user.role === 'professor') {
                return $http.post("/api/updateProfessor/" + user.userid, user)
                    .then(function (response) {
                        return response.data;
                    });
            }
            else if (user.role === 'admin') {
                return $http.post("/api/updateAdmin/" + user.userid, user)
                    .then(function (response) {
                        return response.data;
                    });
            }
        }

        /**
         * this method retrieves all the professor registration requests
         */
        function findAllProfessorRequests() {
            return $http.get("/api/getAllUsers")
                .then(function (response) {
                    return response.data;
                });
        }

        /**
         * this method retrieves all preofessor record available in the database
         */
        function getAllProfessors() {
            return $http.get("/api/getAllProfessors")
                .then(function (response) {
                    return response.data;
                });
        }

        /**
         * this method retrieves all Student record available in the database
         */
        function getAllStudents() {
            return $http.get("/api/getAllStudents")
                .then(function (response) {
                    return response.data;
                });
        }

        /**
         * this method is used for admin user to accept a particular professor registration request
         * @param profId represents the unique id for a professor
         */
        function acceptRequest(profId) {
            return $http.get("/api/acceptRequest/" + profId)
                .then(function (response) {
                    return response.data;
                });
        }

        /**
         * this method is used for admin user to reject a particular professor registration request
         * @param profId represents the unique id for a professor
         */
        function rejectRequest(profId) {
            return $http.get("/api/rejectRequest/" + profId)
                .then(function (response) {
                    return response.data;
                });
        }

        /**
         * this method is used by admin to change the user role in the database
         * @param user represents the user object
         */
        function changeRole(user) {
            return $http.post("/api/changeRole/" + user.userid + "/" + user.newrole)
                .then(function (response) {
                    return response.data
                })
        }

        /**
         * this method deletes the passed user object in the database
         * @param usr represents the user object
         */
        function deleteUser(usr) {
            return $http.post("/api/deleteUser/" + usr.username)
                .then(function (response) {
                    return response.data;
                })
        }

        /**
         * this method retrieves teh user details who is currently at loggedin session
         */
        function loggedIn() {
            return $http.get("/api/isLoggedIn")
                .then(function (response) {
                    return response.data;
                })
        }

        /**
         * this method is to check if the current session user is a student or not
         */
        function isStudent() {
            return $http.get("/api/isStudent")
                .then(function (response) {
                    return response.data;
                })
        }

        /**
         * this method is to check if the current session user is a professor or not
         */
        function isProfessor() {
            return $http.get("/api/isProfessor")
                .then(function (response) {
                    return response.data;
                })
        }

        /**
         * this method is to check if the current session user is an admin or not
         */
        function isAdmin() {
            return $http.get("/api/isAdmin")
                .then(function (response) {
                    return response.data;
                })
        }

    }
})();
