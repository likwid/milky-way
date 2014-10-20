/*global
    angular: true,
    _: true,
    R: true,
    d3: true
*/

(function () {
    "use strict";

    var deps = ["$http", "$q", "asyncCache"];

    function Directive($http, $q, asyncCache) {
        function uriAsPromise(res) {
            var df = $q.defer();
            $http.get(res)
                .success(function (response) { df.resolve(response); })
                .error(function (err) { df.reject(err); });
            return df.promise;
        }

        function firstTimeCachedUri(val, res) {
            return val || asyncCache.timed(res, 60000, R.lPartial(uriAsPromise, res));
        }
        return {
            restrict: "E",
            scope: {},
            templateUrl: "/partials/stats-modal.html",
            replace: true,
            link: function ($scope, elem) {
                var sizeAsPercent = R.curry(function (total, d) {
                    return ((d[1] / total) * 100).toFixed(0) + "%";
                });

                function showModal(data) {
                    elem
                        .modal("setting", {
                            onHidden: function () {
                                d3.select(elem[0])
                                    .select(".spread")
                                    .selectAll("div.progress-segment")
                                    .remove();
                                d3.select(elem[0])
                                    .select(".legend")
                                    .selectAll("td")
                                    .remove();
                            },
                            duration: 250
                        })
                        .modal("show");
                    return data;
                }

                function getColors(data) {
                    return asyncCache
                            .get("github-colors.json")
                            .then(R.rPartial(firstTimeCachedUri, "github-colors.json"))
                            .then(function (colors) { return [colors, data]; });
                }

                function backgroundColor(colors, d) {
                    return colors[d[0]] || "#aaaaaa";
                }

                function textColor(colors, d) {
                    var rgbMatch = /#([0-9a-fA-F][0-9a-fA-F])([0-9a-fA-F][0-9a-fA-F])([0-9a-fA-F][0-9a-fA-F])/,
                        colorStrings = rgbMatch.exec(backgroundColor(colors, d)).slice(1, 4),
                        channels = R.map(R.rPartial(parseInt, 16), colorStrings),
                        percievedLuminance = 0.299 * channels[0] + 0.587 * channels[1] + 0.114 * channels[2];

                    return percievedLuminance < 128 ? "#FFF" : "#000";
                }

                function drawData(pass) {
                    var languageColors = pass[0],
                        totalBytes = R.sum(R.values(pass[1])),
                        d3Data = R.sort(function (a, b) { return b[1] - a[1]; },
                                        R.zip(R.keys(pass[1]), R.values(pass[1]))),
                        labelTemplate = _.template("${name} <span class=\"detail\">${perc}</span>"),
                        elements = d3.select(elem[0])
                                      .select(".spread")
                                      .selectAll("div.progress-segment")
                                      .data(d3Data),
                        wrapWithColors = R.rPartial(R.lPartial, languageColors);

                    d3.select(elem[0])
                        .select(".legend")
                        .selectAll("td")
                        .data(d3Data)
                        .enter()
                        .append("td")
                        .append("span")
                        .classed({ui: true, label: true})
                        .html(function (d) {
                            return labelTemplate({
                                name: d[0],
                                perc: sizeAsPercent(totalBytes, d)
                            });
                        })
                        .style("background", wrapWithColors(backgroundColor))
                        .style("color", wrapWithColors(textColor));

                    elements
                        .enter()
                        .append("div")
                        .style("width", "0%")
                        .style("background", wrapWithColors(backgroundColor))
                        .classed("progress-segment", true);

                    elements
                        .transition()
                        .duration(500)
                        .style("width", sizeAsPercent(totalBytes));
                }

                /*jslint unparam: true*/
                $scope.$on("show-stats", function (e, langUri) {
                    asyncCache
                        .get(langUri)
                        .then(R.rPartial(firstTimeCachedUri, langUri))
                        .then(showModal)
                        .then(getColors)
                        .then(drawData);
                });
                /*jslint unparam: false*/
            }
        };
    }

    angular
        .module("milkyWay")
        .directive("statsModal", deps.concat(Directive));
}());