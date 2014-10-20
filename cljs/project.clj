(defproject milkway "0.0.1"
  :description "FIXME: write description"
  :dependencies [[org.clojure/clojure "1.6.0"]
                 [org.clojure/clojurescript "0.0-2371"]
                 [swiss-arrows "1.0.0"]]
  :plugins [[lein-cljsbuild "1.0.3"]]
  :source-paths ["src/clj"]
  :cljsbuild {:builds [
    {:source-paths ["src/cljs"]
     :compiler {
        :output-dir "resources/build-min"
        :output-to "resources/milkyway.js"
        :source-map "resources/milkyway.js.map"
        :optimizations :whitespace
        :externs ["externs/angular.js"
                  "externs/d3.js"
                  "externs/semanticui.js"]}}
    {:source-paths ["src/cljs"]
     :compiler {
        :output-dir "resources/build"
        :output-to "resources/milkyway.min.js"
        :source-map "resources/milkyway.min.js.map"
        :optimizations :advanced
        :pretty-print false
        :preamble ["preamble/jquery.min.js"
                   "preamble/semantic.min.js"
                   "preamble/angular.min.js"
                   "preamble/angular-route.min.js"
                   "preamble/d3.min.js"
                   "preamble/q.min.js"]
        :externs ["externs/angular.js"
                  "externs/d3.js"
                  "externs/semanticui.js"]}}]})