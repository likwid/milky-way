(ns milkyway.config
  (:require-macros [clojangular.angular :as ng]))

(def ^:private specs [{
  :route "/"
  :templateUrl "partials/default.html"
  :controller "DefaultController"
}{
  :route "/user/:username"
  :templateUrl "partials/user.html"
  :controller "UserController"
}{
  :route :default
  :redirectTo "/"
}])

(defn- assignRoute [provider route spec]
  (if (keyword? route)
    (.otherwise provider spec)
    (.when provider route (clj->js spec))))

(ng/config [$routeProvider]
  (doseq [spec specs]
    (assignRoute $routeProvider (:route spec) (dissoc spec :route))))