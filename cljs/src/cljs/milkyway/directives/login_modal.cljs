(ns milkyway.directives.login-modal
  (:require-macros [clojangular.angular :as ng]))

(ng/directive "loginModal" []
  (clj->js {:restrict "E"
            :scope (js-obj "user" "=")
            :templateUrl "/partials/login-modal.html"
            :replace true
            :link (fn [$scope elem]
                    (doto $scope
                      (aset "vm" (js-obj "ghName" nil))
                      (aset "submitUser" (fn [] (.modal elem "hide")
                                                (.$emit $scope "user-select" (-> $scope (aget "vm") (aget "ghName"))))))
                    (if (not (aget $scope "user")) (.modal elem "show")))}))

