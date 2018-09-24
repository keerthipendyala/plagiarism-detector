/**
 * This services sends https requests to the server for all semester related manipulations
 */
(function () {
    angular.module("CopyCatch")
        .factory('semesterService', semesterService);

    function semesterService($http) {
        var api = {
            "getAllSemesters": getAllSemesters,
        };
        return api;

        /**
         * this method retrieves all semesters that has been available for our College
         */
        function getAllSemesters() {
            return $http.get("/api/getAllSemester")
                .then(function (response) {
                    return response.data;
                });
        }

    }
})();