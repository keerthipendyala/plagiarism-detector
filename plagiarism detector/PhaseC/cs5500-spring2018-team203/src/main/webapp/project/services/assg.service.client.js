/**
 * This services sends https requests to the server for all assignment related manipulations
 */
(function () {
    angular.module("CopyCatch")
        .factory('assgService', assgService);

    function assgService($http) {
        var api = {
            "getAllAssg": getAllAssg,
            "submitAssg": submitAssg,
            "updateUserCourses": updateUserCourses,
            "getAllAssignments": getAllAssignments,
            "createAssignment": createAssignment,
            "updateAssignment": updateAssignment,
            "deleteAssignment": deleteAssignment,
            "getAssignmentById": getAssignmentById
        };
        return api;

        /**
         * this method retrieves all assignment objects for a particular course
         * @param cid represents the courseid of a paricular course
         */
        function getAllAssg(cid) {
            return $http.get("/api/getAllAssignments/" + cid)
                .then(function (response) {
                    return response.data;
                });
        }

        /**
         * this method retrieves all courses that a particular user enrolled for
         * @param assg represents the asignemnt object
         */
        function submitAssg(assg) {
            return $http.get("/api/getAllCourses/" + usr.role + "/" + uid)
                .then(function (response) {
                    return response.data;
                });
        }

        /**
         * this method adds the provided courses for the particular user
         * @param user represents the user object for whom we need to add the course
         * @param courses represents the list of courses that has to be added for this user
         */
        function updateUserCourses(user, courses) {
            uid = usr.role === 'student' ? usr.studentid : usr.profid
            return $http.post("/api/addCourses/" + usr.role + "/" + uid, courses)
                .then(function (response) {
                    return response.data;
                });
        }

        /**
         *  this method retrieves all assignment objects for a particular cour
         * @param courseId represents the id for a particular course
         */
        function getAllAssignments(courseId) {
            return $http.get("/api/getAllAssignments/" + courseId)
                .then(function (response) {
                    return response.data;
                });
        }

        /**
         * this method creates the assignment object in the database
         * @param assignment represents the assignment object
         */
        function createAssignment(assignment) {
            return $http.post("/api/createAssignment/", assignment)
                .then(function (response) {
                    return response.data;
                });
        }

        /**
         * this method updates the assignment object in the database
         * @param assignment represents the assignment object
         */
        function updateAssignment(assignment) {
            return $http.post("/api/updateAssignment/", assignment)
                .then(function (response) {
                    return response.data;
                });
        }

        /**
         * this method delete the assignment object in the database
         * @param assignmentId represents the assignment id for which we need to retrieve the assignment object
         */
        function deleteAssignment(assignmentId) {
            return $http.delete("/api/deleteAssignment/" + assignmentId)
                .then(function (response) {
                    return response.data;
                });
        }


        /**
         * this method retrieves the assignment object
         * @param aid represents the assignment id for which we need to retrieve the assignment object
         */
        function getAssignmentById(aid) {
            return $http.get("/api/getAssignment/" + aid)
                .then(function (response) {
                    return response.data;
                });
        }
    }
})();