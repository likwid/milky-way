(ns milkyway.core
  (:require-macros [clojangular.angular :as ng]))

(defn ^:export main []
  (ng/module "milkyWay" ["ngRoute"]
    milkyway.config
    milkyway.controllers.default
    milkyway.controllers.user))
