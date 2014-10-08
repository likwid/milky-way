/*global
    angular: true,
    _: true
*/

(function () {
    "use strict";

    var directive = {
        restrict: "E",
        scope: {user: "="},
        templateUrl: "/partials/login-modal.html",
        replace: true,
        link: function ($scope, elem) {
            $scope.vm = {
                ghName: null
            };

            if (!$scope.user) {
                elem.modal("show");
            }

            $scope.submitUser = function () {
                $scope.user = $scope.vm.ghName;
                elem.modal("hide");
            };
        }
    };

    angular
        .module("milkyWay")
        .directive("loginModal", _.constant(directive));
}());