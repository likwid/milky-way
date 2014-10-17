(ns milkyway.core
  (:require-macros [clojangular.angular :as ng]))

(defn main []
  (ng/module "milkyWay" ["ngRoute"]
    milkyway.config
    milkyway.directives.login-modal
    milkyway.controllers.default))

(main)
