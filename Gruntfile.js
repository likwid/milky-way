module.exports = function (grunt) {
    "use strict";
    grunt.loadNpmTasks("grunt-contrib-concat");

    grunt.initConfig({
        concat: {
            dist: {
                src: [
                    "bower_components/jquery/dist/jquery.js",
                    "bower_components/semantic-ui/build/packaged/javascript/semantic.min.js",
                    "bower_components/angular/angular.min.js",
                    "bower_components/angular-route/angular-route.min.js",
                    "bower_components/d3/d3.min.js",
                    "bower_components/lodash/dist/lodash.min.js",
                    "bower_components/ramda/ramda.min.js",
                    "bower_components/bilby.js/bilby-min.js",
                    "scripts/milkyway.js",
                    "scripts/controllers/default.js",
                    "scripts/directives/login-modal.js",
                    "scripts/directives/stats-modal.js",
                    "scripts/services/cache.js"
                ],
                dest: "build/all.js"
            }
        }
    });

    grunt.registerTask("default", ["concat"]);
};