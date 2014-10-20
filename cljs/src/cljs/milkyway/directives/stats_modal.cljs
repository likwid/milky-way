(ns milkyway.directives.stats-modal
  (:require [milkyway.async-cache :as cache])
  (:require-macros [clojangular.angular :as ng]
                   [swiss.arrows :refer [-<>]]))

(defn- destroy-modal-data [elem]
  (doto (.select js/d3 (.get elem 0))
        (-> (.select ".spread")
            (.selectAll "div.progress-segment")
            (.remove))
        (-> (.select ".legend")
            (.selectAll "td")
            (.remove))))

(defn- show-modal [elem data]
  (-> elem
      (.modal "setting" (clj->js {:onHidden (partial destroy-modal-data elem) :duration 250}))
      (.modal "show"))
  data)

(defn- uri-as-promise [$q $http resource]
  (let [df (.defer $q)]
    (-> $http
          (.get resource)
          (.success (fn [response] (.resolve df response)))
          (.error (fn [error] (.reject df error))))
    (aget df "promise")))

(defn- first-time-cached-uri [$q $http val res]
  (or val (cache/timed res 60000 (partial uri-as-promise $q $http res))))

(defn- get-colors [$q $http data]
  (-> (cache/get-val "github-colors.json")
      (.then #(first-time-cached-uri $q $http % "github-colors.json"))
      (.then #(array % data))))


(defn- size->percent [total d]
  (-> d
      (second)
      (/ total)
      (* 100)
      (.toFixed 0)
      (str "%")))

(defn- background-color [colors d]
  (-<> colors
      (aget <> (first d))
      (or <> "#aaaaaa")))

(defn- text-color [colors d]
  (let [[r g b] (-<> #"([0-9a-fA-F][0-9a-fA-F])([0-9a-fA-F][0-9a-fA-F])([0-9a-fA-F][0-9a-fA-F])"
                      (.exec <> (background-color colors d))
                      (.slice <> 1 4)
                      (map #(js/parseInt % 16) <>))
        luminance (+ (* 0.299 r) (* 0.587 g) (* 0.114 b))]
    (if (< luminance 128)
        "#FFF"
        "#000")))

(defn- draw-data [elem [colors data]]
  (let [better-data (js->clj data)
        total-bytes (reduce + (vals better-data))
        d3-data (reverse (sort-by last (map vector (keys better-data) (vals better-data))))
        elements (-> js/d3
                     (.select (.get elem 0))
                     (.select ".spread")
                     (.selectAll "div.progress-segment")
                     (.data (clj->js d3-data)))]

    (-> js/d3
        (.select (.get elem 0))
        (.select ".legend")
        (.selectAll "td")
        (.data (clj->js d3-data))
        (.enter)
        (.append "td")
        (.append "span")
        (.classed (js-obj "ui" true "label" true))
        (.html #(str (first %) " <span class=\"detail\">" (size->percent total-bytes %) "</span>"))
        (.style "background" (partial background-color colors))
        (.style "color" (partial text-color colors)))
    (doto elements
          (-> (.enter)
              (.append "div")
              (.style "width" "0%")
              (.style "background" (partial background-color colors))
              (.classed "progress-segment" true))
          (-> (.transition)
              (.duration 500)
              (.style "width" (partial size->percent total-bytes))))))

(defn- show-stats [$q $http elem $e uri]
  (-> (cache/get-val uri)
      (.then #(first-time-cached-uri $q $http % uri))
      (.then (partial show-modal elem))
      (.then (partial get-colors $q $http))
      (.then (partial draw-data elem))))

(ng/directive "statsModal" [$http $q]
  (clj->js {:restrict "E"
            :scope (js-obj)
            :templateUrl "/partials/stats-modal.html"
            :replace true
            :link (fn [$scope elem]
                    (.$on $scope "show-stats" (partial show-stats $q $http elem)))}))
