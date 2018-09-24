/**
 * This services sends https requests to the server for all course related manipulations
 */
(function () {
    angular.module("CopyCatch")
        .factory('courseService', courseService);

    function courseService($http) {
        var api = {
            "getAllCourses": getAllCourses,
            "getEnrolledCourses": getEnrolledCourses,
            "updateUserCourses": updateUserCourses,
            "getAllCoursesForProfessor": getAllCoursesForProfessor,
            "getCourseForProfessorById": getCourseForProfessorById,
            "updateCourse": updateCourse,
            "createCourse": createCourse,
            "deleteCourse": deleteCourse,
            "getCoursesForStudent": getCoursesForStudent,
            "getUnregisteredCourses": getUnregisteredCourses,
            "enroll": enroll
        };
        return api;

        /**
         * this method retrieves all courses available for a particular user
         * @param usr represents the user object
         */
        function getAllCourses(usr) {
            return $http.get("/api/getAllCourses/" + usr.role)
                .then(function (response) {
                    return response.data;
                });
        }

        /**
         * this method retrieves all courses that a particular user has enrolled for
         * @param usr represents the user object
         */
        function getEnrolledCourses(usr) {
            uid = usr.role === 'student' ? usr.studentid : usr.professorid;
            return $http.get("/api/getAllCoursesForAId/" + uid + "/" + usr.role)
                .then(function (response) {
                    return response.data;
                });
        }

        /**
         * this method update course list of an user(student/ professor)
         * @param usr represents the user object
         * @param courses represents the list of courses that has to be added for this user
         */
        function updateUserCourses(usr, courses) {
            uid = usr.role === 'student' ? usr.studentid : usr.professorid;
            if (usr.role === 'professor') {
                for (i = 0; i < courses.length; i++) {
                    courses[i].profid = usr.professorid;
                    courses[i].capacity = 0;
                }
            }
            apicall = usr.role === 'student' ? "/api/addCourses/" : "/api/addProfessorCourse/"
            return $http.post(apicall + uid, courses)
                .then(function (response) {
                    return response.data;
                });
        }


        /**
         * this method retrieves all the courses for a particular user under a particular semester
         * @param sid represents the semesterid
         * @param uid represents the userid
         */
        function getAllCoursesForProfessor(sid, uid) {
            return $http.get("/api/getAllCourses/" + sid + "/" + uid)
                .then(function (response) {
                    return response.data;
                });
        }

        /**
         * this method retrieves all courses for a particular professor under a particular semester
         * @param sid represents the semesterid
         * @param cid represents the courseid
         * @param pid represents the professorid
         */
        function getCourseForProfessorById(sid, cid, pid) {
            return $http.get("/api/getCourse/" + sid + "/" + cid + "/" + pid)
                .then(function (response) {
                    return response.data;
                });
        }

        /**
         * this method updates the provided course in the database
         * @param course represents the course object
         */
        function updateCourse(course) {
            return $http.post("/api/updateCourse", course)
                .then(function (response) {
                    return response.data;
                });
        }

        /**
         * this method creates the provided course in the database
         * @param course represents the course object
         */
        function createCourse(course) {
            return $http.post("/api/createCourse", course)
                .then(function (response) {
                    return response.data;
                });
        }

        /**
         * this method deletes the particular course in the database
         * @param courseId represents the courseid
         */
        function deleteCourse(courseId) {
            return $http.delete("/api/deleteCourse/" + courseId)
                .then(function (response) {
                    return response.data;
                });
        }

        /**
         * this method retrieves all courses that a particular student has enrolled for
         * @param uid represents the userid
         * @param sid represents the semesterid
         */
        function getCoursesForStudent(uid, sid) {
            return $http.get("/api/getAllStudentCourses/" + uid + "/" + sid)
                .then(function (response) {
                    return response.data;
                });

        }

        /**
         * this method retrieves all courses that a student can register for the particular semester
         * @param uid represents the userid
         * @param sid represents the semesterid
         */
        function getUnregisteredCourses(uid, sid) {
            return $http.get("/api/getUnregisteredCourses/" + uid + "/" + sid)
                .then(function (response) {
                    return response.data;
                });
        }

        /**
         * this method is used to enroll the user for the provided course
         * @param uid represents the userid
         * @param cid represents the courseid
         */
        function enroll(uid, cid) {
            return $http.get("/api/addCourse/" + uid + "/" + cid)
                .then(function (response) {
                    return response.data;
                });
        }
    }
})();