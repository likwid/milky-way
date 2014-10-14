/*global
    angular: true,
    _: true,
    R: true
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
                elem.modal("hide");
                $scope.$emit("user-select", $scope.vm.ghName);
            };
        }
    };

    angular
        .module("milkyWay")
        .directive("loginModal", R.always(directive));
}());