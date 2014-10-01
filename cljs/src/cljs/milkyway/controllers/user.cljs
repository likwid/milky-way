(ns milkyway.controllers.user
  (:require-macros [clojangular.angular :as ng])
  (:use [milkyway.util :only [vm-cycle]]))

(ng/controller "UserController" [$scope $routeParams $http]
  (letfn [(show-panel [which]
            (aset $scope "currentView" which))
          (got-starred [response]
            (vm-cycle $scope (fn [vm] {:results response :requestCompleted true})))]
    (let [username (aget $routeParams "username")
          gh-api-uri (str "https://api.github.com/users/" username "/starred?per_page=100")]
      (aset $scope "vm" (js-obj "username" username
                                "results" (array)
                                "requestCompleted" false))
      (aset $scope "hasResults" #(not (empty? (-> $scope (aget "vm")
                                                         (aget "results")))))
      (aset $scope "showStarred" (partial show-panel "starred"))
      (aset $scope "showStatistics" (partial show-panel "statistics"))
      (-> $http
        (.get gh-api-uri)
        (.success got-starred)))))
