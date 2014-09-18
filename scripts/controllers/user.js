/*global
    angular: true,
    _: true
*/
(function () {
    "use strict";
    var deps = ["$scope", "$routeParams", "$http"],
        ghApiUri = _.template("https://api.github.com/users/${username}/starred?per_page=100");
    function Controller($scope, $routeParams, $http) {
        $scope.vm = {
            username: $routeParams.username,
            results: [],
            requestCompleted: false
        };

        $scope.hasResults = function() {
            return !_.isEmpty($scope.vm.results);
        };

        $scope.showStarred = function() {
            $scope.currentView = "starred";
        };

        $scope.showStatistics = function() {
            $scope.currentView = "statistics";
        };

        $http.get(ghApiUri($routeParams))
            .success(function (response, status, headers) {
                $scope.vm.results = response;
                $scope.vm.requestCompleted = true;
            });
    }
    angular
        .module("milkyWay")
        .controller("UserController", deps.concat(Controller));
}());