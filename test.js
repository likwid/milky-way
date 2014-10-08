/*global _: true*/
_ = require("lodash");

function expand(l) {
    "use strict";
    console.log(l);
    return _.flatten(_.map(l, function (item) {
        var base = item[0],
            rest = item[1];
        return _.isEmpty(rest) ? item : expand(_.map(rest, function (letter, index) {
            var removed = rest.slice(0, index) + rest.slice(index + 1);
            return [base + letter, removed];
        }));
    }), true);
}

function permutations(s) {
    "use strict";
    var perms = _.filter(expand([["", s]]), function (x) { return !_.isEmpty(x); }),
        obj = _.zipObject(perms, perms);

    return _.keys(obj);
}

console.log(permutations("jeff"));