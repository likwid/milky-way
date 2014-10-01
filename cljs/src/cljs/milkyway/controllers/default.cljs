(ns milkyway.controllers.default
    (:require-macros [clojangular.angular :as ng])
    (:use [milkyway.util :only [obj->map]]))

(ng/controller "DefaultController" [$scope $location]
  (aset $scope "vm" (js-obj "ghName" ""))
  (aset $scope "redirectToUser" (fn []
                                  (let [vm (obj->map (aget $scope "vm"))
                                        uri (str "/user/" (:ghName vm))]
                                    (.path $location uri)))))