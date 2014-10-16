/*global
    angular: true,
    _: true
*/

(function () {
    "use strict";
    function Service($q) {
        var cache = {};

        function timedExpiry(time) {
            var then = (new Date()).getTime();
            return function () {
                var now = (new Date()).getTime();
                return now - then > time;
            };
        }

        function timedExpiryPolicy(time) {
            return _.partial(timedExpiry, time);
        }

        function ensureFresh(val) {
            var expired = _.has(cache[val], "expired") ? cache[val].expired() : true;
            return expired ? $q.when(cache[val].refresh())
                .then(function (newVal) {
                    cache[val] = {
                        policy: cache[val].policy,
                        refresh: cache[val].refresh,
                        value: newVal,
                        expired: cache[val].policy()
                    };
                    return newVal;
                }) : $q.when(cache[val].value);
        }

        function get(val) {
            return _.has(cache, val) ? ensureFresh(val) : $q.when(undefined);
        }

        function timed(name, time, refresh) {
            cache[name] = {
                policy: timedExpiryPolicy(time),
                refresh: refresh
            };
            return get(name);
        }

        _.merge(this, {
            timed: timed,
            get: get
        });
    }

    angular
        .module("milkyWay")
        .service("asyncCache", ["$q", Service]);
}());