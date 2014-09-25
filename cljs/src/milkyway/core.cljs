(ns milkyway.core
  (:require [milkyway.controllers.default :as default]
            [milkyway.controllers.user :as user]))

(defn milkway-config [$routeProvider]
  (-> $routeProvider
    (.when "/" (clj->js {:templateUrl "partials/default.html"
                         :controller  "DefaultController"}))
    (.when "/user/:username" (clj->js {:templateUrl "partials/user.html"
                                       :controller  "UserController"}))
    (.otherwise (clj->js {:redirectTo "/"}))))

(defn ^:export main []
  (-> js/angular
    (.module "milkyWay" (clj->js ["ngRoute"]))
    (.config (clj->js ["$routeProvider" milkway-config]))
    (.controller "DefaultController" default/default-controller)
    (.controller "UserController" user/user-controller)))
