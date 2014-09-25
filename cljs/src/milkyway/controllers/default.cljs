(ns milkyway.controllers.default
    (:require [milkyway.util :as util]))

(defn- redirect-to-user [$scope $location]
    (let [vm (util/js->clj (aget $scope "vm"))
          uri (str "/user/" (:ghName vm))]
        (.path $location uri)))

(defn- controller [$scope $location]
    (aset $scope "vm" (clj->js {:ghName ""}))
    (aset $scope "redirectToUser" (partial redirect-to-user $scope $location)))

(def default-controller (clj->js ["$scope" "$location" controller]))