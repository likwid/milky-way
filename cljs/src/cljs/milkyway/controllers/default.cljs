(ns milkyway.controllers.default
    (:require-macros [clojangular.angular :as ng]))

(ng/controller "DefaultController" [$scope $location]
    (aset $scope "vm" (js-obj "ghName" ""))
    (aset $scope "redirectToUser" #(.path $location (str "/user/" (-> $scope (aget "vm")
                                                                             (aget "ghName"))))))