(ns milkyway.controllers.user
  (:require-macros [clojangular.angular :as ng])
  (:use [milkyway.util :only [vm-cycle]]))

(ng/controller "UserController" [$scope $routeParams $http]
  (letfn [(got-starred [response] (vm-cycle $scope (constantly {:results response
                                                                :requestCompleted true})))]
    (let [username (aget $routeParams "username")
          gh-api-uri (str "https://api.github.com/users/" username "/starred?per_page=100")]
      (doto $scope
        (aset "vm" (js-obj "username" username
                           "results" (array)
                           "requestCompleted" false))
        (aset "hasResults" #(not (empty? (-> $scope (aget "vm")
                                                    (aget "results")))))
        (aset "showPanel" (fn [which] (aset $scope "currentView" which))))
      (-> $http (.get gh-api-uri)
                (.success got-starred)))))
