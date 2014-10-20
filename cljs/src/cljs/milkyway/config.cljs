(ns milkyway.config
  (:require-macros [clojangular.angular :as ng]))

(def ^:private specs [{
  :route "/user/:user?"
  :templateUrl "partials/default.html"
  :controller "DefaultController"
}{
  :route :default
  :redirectTo "/user/"
}])

(defn- assignRoute [provider route spec]
  (if (keyword? route)
    (.otherwise provider (clj->js spec))
    (.when provider route (clj->js spec))))

(ng/config [$routeProvider]
  (doseq [spec specs]
    (assignRoute $routeProvider (:route spec) (dissoc spec :route))))