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
                .when("/user/:user?", {
                    controller: "DefaultController",
                    templateUrl: "partials/default.html",
                    reloadOnSearch: false
                })
                .otherwise({
                    redirectTo: "/user/"
                });
        }]);
}());