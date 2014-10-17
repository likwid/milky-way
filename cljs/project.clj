(defproject milkway "0.0.1"
  :description "FIXME: write description"
  :dependencies [[org.clojure/clojure "1.6.0"]
                 [org.clojure/clojurescript "0.0-2371"]]
  :plugins [[lein-cljsbuild "1.0.3"]]
  :source-paths ["src/clj"]
  :cljsbuild {:builds [
    {:source-paths ["src/cljs"]
     :compiler {
        :output-dir "resources/build-min"
        :output-to "resources/milkyway.js"
        :source-map "resources/milkyway.js.map"
        :optimizations :whitespace
        :externs ["angular_externs.js"]}}
    {:source-paths ["src/cljs"]
     :compiler {
        :output-dir "resources/build"
        :output-to "resources/milkyway.min.js"
        :source-map "resources/milkyway.min.js.map"
        :optimizations :advanced
        :pretty-print false
        :externs ["angular_externs.js"]}}]})