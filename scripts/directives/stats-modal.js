/*global
    angular: true,
    _: true,
    R: true,
    d3: true
*/

(function () {
    "use strict";

    var deps = ["$http", "$q", "asyncCache", "languageColors"];

    function Directive($http, $q, asyncCache, languageColors) {
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
                            }
                        })
                        .modal("show");
                    return data;
                }

                function backgroundColor(d) {
                    return languageColors[d[0]] || "#aaaaaa";
                }

                function textColor(d) {
                    var rgbMatch = /#([0-9a-fA-F][0-9a-fA-F])([0-9a-fA-F][0-9a-fA-F])([0-9a-fA-F][0-9a-fA-F])/,
                        colorStrings = rgbMatch.exec(backgroundColor(d)).slice(1, 4),
                        colors = R.map(R.rPartial(parseInt, 16), colorStrings),
                        percievedLuminance = 0.299 * colors[0] + 0.587 * colors[1] + 0.114 * colors[2];

                    return percievedLuminance < 128 ? "#FFF" : "#000";
                }

                function drawData(data) {
                    var totalBytes = R.sum(R.values(data)),
                        d3Data = _.sortBy(R.zip(R.keys(data), R.values(data)), function (d) { return d[1]; }).reverse(),
                        legend = d3.select(elem[0])
                                    .select(".legend")
                                    .selectAll("td")
                                    .data(d3Data),
                        labelTemplate = _.template("${name} <span class=\"detail\">${perc}</span>"),
                        elements = d3.select(elem[0])
                                      .select(".spread")
                                      .selectAll("div.progress-segment")
                                      .data(d3Data);

                    legend
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
                        .style("background", backgroundColor)
                        .style("color", textColor);

                    elements
                        .enter()
                        .append("div")
                        .style("width", "0%")
                        .style("background", backgroundColor)
                        .style("color", textColor)
                        .classed("progress-segment", true);

                    elements
                        .transition()
                        .duration(1000)
                        .style("width", sizeAsPercent(totalBytes));
                }

                /*jslint unparam: true*/
                $scope.$on("show-stats", function (e, langUri) {
                    console.log(langUri);
                    asyncCache
                        .get(langUri)
                        .then(R.rPartial(firstTimeCachedUri, langUri))
                        .then(showModal)
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