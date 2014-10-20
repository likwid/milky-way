(ns milkyway.core
  (:require-macros [clojangular.angular :as ng]))

(defn main []
  (ng/module "milkyWay" ["ngRoute"]
    milkyway.config
    milkyway.directives.login-modal
    milkyway.directives.stats-modal
    milkyway.services.async-cache
    milkyway.controllers.default))

(main)
