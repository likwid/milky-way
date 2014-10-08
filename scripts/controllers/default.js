/*global
    angular: true,
    _: true
*/
(function () {
    "use strict";
    var deps = ["$scope", "$http"],
        ghApiUri = _.template("https://api.github.com/users/${username}/starred?per_page=100");
    function Controller($scope, $http) {
        $scope.vm = {
            user: null,
            results: [],
            requestCompleted: false
        };

        $scope.hasResults = function () {
            return !_.isEmpty($scope.vm.results);
        };

        $scope.showPanel = function (which) {
            $scope.currentView = which;
        };

        $scope.$watch("vm.user", function (val) {
            if (val) {
                $http.get(ghApiUri({username: val}))
                    .success(function (response) {
                        $scope.vm.results = response;
                        $scope.vm.requestCompleted = true;
                    });
            }
        });

    }
    angular
        .module("milkyWay")
        .controller("DefaultController", deps.concat(Controller));
}());