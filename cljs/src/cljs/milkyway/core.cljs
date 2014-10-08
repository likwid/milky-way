(ns milkyway.core
  (:require-macros [clojangular.angular :as ng]))

(defn main []
  (ng/module "milkyWay" ["ngRoute"]
    milkyway.config
    milkyway.controllers.default
    milkyway.controllers.user))

(main)
