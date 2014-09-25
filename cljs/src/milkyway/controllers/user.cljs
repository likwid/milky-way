(ns milkyway.controllers.user
  (:require [milkyway.util :as util]))

(defn- gh-api-uri [username]
  (str "https://api.github.com/users/" username "/starred?per_page=100"))

(defn- has-results [$scope]
  (let [vm (util/js->clj (aget $scope "vm"))]
    (not (empty? (:results vm)))))

(defn- show-starred [$scope]
  (aset $scope "currentView" "starred"))

(defn- show-statistics [$scope]
  (aset $scope "currentView" "statistics"))

(defn- got-starred [$scope response]
  (util/vm-cycle $scope (fn [vm] {:results response :requestCompleted true})))

(defn- controller [$scope $routeParams $http]
  (doto $scope
    (aset "vm" (clj->js {:username (aget $routeParams "username")
                         :results []
                         :requestCompleted false}))
    (aset "hasResults" (partial has-results $scope))
    (aset "showStarred" (partial show-starred $scope))
    (aset "showStatistics" (partial show-statistics $scope)))
  (-> $http
    (.get (-> $routeParams (aget "username") gh-api-uri))
    (.success (partial got-starred $scope))))

(def user-controller (clj->js ["$scope" "$routeParams" "$http" controller]))