/*global
    angular: true,
    _: true,
    bilby: true,
    R: true
*/
(function () {
    "use strict";
    var deps = ["$scope", "$http", "$routeParams", "$location"],
        ghApiUri = _.template("https://api.github.com/users/${username}/starred?per_page=100");
    function Controller($scope, $http, $routeParams, $location) {
        $scope.R = R;
        var hasUser = R.identity,
            noUser = R.alwaysTrue;

        function updateUser(user) {
            $http.get(ghApiUri({username: user}))
                .success(function (response) {
                    $scope.vm = $scope.vm
                        .property("results", response)
                        .property("currentView", "starred");
                });
        }

        /*jslint unparam:true*/
        function userSelect($e, user) {
            $location.path("/user/" + user);
        }
        /*jslint unparam:false*/

        $scope.vm = bilby.environment()
            .property("user", $routeParams.user)
            .property("currentView", null)
            .property("results", null)
            .property("hasResults", false)
            .property("showPanel", function (which) {
                $scope.vm = $scope.vm
                    .property("currentView", which);
            })
            .method("userWatch", hasUser, updateUser)
            .method("userWatch", noUser, _.noop);

        $scope.$watch("vm.user", $scope.vm.userWatch);
        $scope.$on("user-select", userSelect);

    }
    angular
        .module("milkyWay")
        .controller("DefaultController", deps.concat(Controller));
}());