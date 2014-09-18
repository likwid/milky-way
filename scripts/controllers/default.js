/*global
    angular: true,
    _: true
*/
(function () {
    "use strict";
    var deps = ["$scope", "$location"];
    function Controller($scope, $location) {
        $scope.vm = {
            ghName: ""
        };

        $scope.redirectToUser = function () {
            $location.path("/user/" + $scope.vm.ghName);
        };
    }
    angular
        .module("milkyWay")
        .controller("DefaultController", deps.concat(Controller));
}());