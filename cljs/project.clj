(defproject milkway "0.0.1"
  :description "FIXME: write description"
  :url "http://example.com/FIXME"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies [[org.clojure/clojure "1.6.0"]
                 [org.clojure/clojurescript "0.0-2342"]]
  :plugins [[lein-cljsbuild "1.0.3"]]
  :cljsbuild {:builds [
    {:source-paths ["src"]
     :compiler {
        :output-dir "resources/build-min"
        :output-to "resources/milkyway.js"
        :source-map "resources/milkyway.js.map"
        :optimizations :whitespace
        :externs ["angular_externs.js"]}}
    {:source-paths ["src"]
     :compiler {
        :output-dir "resources/build"
        :output-to "resources/milkyway.min.js"
        :source-map "resources/milkyway.min.js.map"
        :optimizations :advanced
        :externs ["angular_externs.js"]}}]})