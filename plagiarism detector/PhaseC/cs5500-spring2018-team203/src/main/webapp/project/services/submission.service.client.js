/**
 * This services sends https requests to the server for all submission related manipulations
 */
(function () {
    angular.module("CopyCatch")
        .factory('submissionService', submissionService);

    function submissionService($http) {
        var api = {
            "submitAssg": submitAssg,
            "subHistory": subHistory,
            "getAllSubmissionsByAllStudents": getAllSubmissionsByAllStudents,
            "compare": compare,
            "getScores": getScores,
            "getViolations": getViolations,
            "getReportLinkById": getReportLinkById,
            "getAllCompareCourses": getAllCompareCourses
        };
        return api;


        /**
         * this method retrieves the report link for the particular id
         * @param id represents the unique id for a report
         */
        function getReportLinkById(id) {
            return $http.get("/api/getReport/" + id)
                .then(function (response) {
                    return response.data;
                });
        }

        /**
         * this method creates the assignment record in the database
         * @param assg represents the assignment object that contains studentid, courseid, assignmentid and assignment details
         */
        function submitAssg(assg) {
            return $http.post("/api/submitAssignment/", assg)
                .then(function (response) {
                    return response.data;
                });
        }

        /**
         * this method retrieves all submissions for a particular student course's assignment
         * @param sid represents the unique id for a student
         * @param cid represents the unique id for a course
         * @param aid represents the unique id for an assignment
         */
        function subHistory(sid, cid, aid) {
            return $http.get("/api/getAllSubmission/" + sid + "/" + cid + "/" + aid)
                .then(function (response) {
                    return response.data;
                });
        }

        /**
         * this method retrieves all the submissions done by all the students for that particular course for that HW
         * @param cid represents the unique id for a course
         * @param aid represents the unique id for an assignment
         */
        function getAllSubmissionsByAllStudents(cid, aid) {
            return $http.get("/api/getAllStudentSubmissions/" + cid + "/" + aid)
                .then(function (response) {
                    return response.data;
                });
        }

        /**
         * this method is to start the scanning of the given list of submissions for plagiarism
         * @param submissions represents the list of submission object
         */
        function compare(submissions) {
            return $http.post("/api/scanassignment/", submissions)
                .then(function (response) {
                    return response.data;
                });
        }

        /**
         * Gets the submissions scores for the given submisisions
         * @param subs Submissions
         */
        function getScores(subs) {
            return $http.post("/api/getSubmissionScores/", subs)
                .then(function (response) {
                    return response.data;
                });
        }

        /**
         * this method retrieves all the violations(plagiarized) summary for that particular professor's courses
         * @param profid represents the unique id for a professor
         */
        function getViolations(profid) {
            return $http.get("/api/getAllViolationSummary/" + profid)
                .then(function (response) {
                    return response.data;
                });
        }

        /**
         * Gets the courses whose assignments need to be compared for plagiarism with given course of the
         * given semester
         * @param semesterId given semester whose submissions need to be compared
         * @param courseId course id of the course whose assignments need to be compared
         * @param courseNo courseNo of the course whose assignments need to be compared
         * @param check true if comparison should consider other sections within same course , false otherwise
         * @param selectedSemesters whose submissions should be retrieved for comparing
         */
        function getAllCompareCourses(semesterId, courseId, courseNo, check, selectedSemesters) {
            return $http.post("/api/semesterSubmissions/" + semesterId + "/" + courseId + "/" + courseNo + "/" + check + "/", selectedSemesters)
                .then(function (response) {
                    return response.data;
                });
        }


    }
})();