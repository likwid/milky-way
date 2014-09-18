/*global
    angular: true,
    _: true
*/
(function () {
    "use strict";

    angular
        .module("milkyWay", ["ngRoute"])
        .config(["$routeProvider", function ($routeProvider) {
            $routeProvider
                .when("/", {
                    templateUrl: "partials/default.html",
                    controller: "DefaultController"
                })
                .when("/user/:username", {
                    templateUrl: "partials/user.html",
                    controller: "UserController"
                })
                .otherwise({
                    redirectTo: "/"
                });
        }]);
}());