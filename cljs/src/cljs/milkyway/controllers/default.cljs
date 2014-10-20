(ns milkyway.controllers.default
  (:require-macros [clojangular.angular :as ng]))

(defn- update-user [$scope $http user]
  (-> $http
      (.get (str "https://api.github.com/users/" user "/starred?per_page=100"))
      (.success (fn [response]
                  (doto (aget $scope "vm")
                    (aset "results" response)
                    (aset "currentView" "starred"))))))

(defn- user-select [$location $e user]
  (.path $location (str "/user/" user)))

(defn- user-watch [$scope $http user]
  (if user (update-user $scope $http user)))

(ng/controller "DefaultController" [$scope $http $routeParams $location]
  (doto $scope
    (aset "vm" (js-obj "user" (aget $routeParams "user")
                       "currentView" nil
                       "results" nil
                       "hasResults" false
                       "showPanel" (fn [which] (aset (aget $scope "vm") "currentView" which))
                       "showStats" (fn [uri] (.$broadcast $scope "show-stats" uri))))
    (.$watch "vm.user" (partial user-watch $scope $http))
    (.$on "user-select" (partial user-select $location))))
