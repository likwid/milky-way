(ns milkyway.services.async-cache
  (:require-macros [clojangular.angular :as ng]))


(defn- timed-expiry [time]
  (let [then (.getTime (js/Date.))]
    #(> (- (.getTime (js/Date.)) then) time)))

(defn- timed-expiry-policy [time]
  #(timed-expiry time))

(defn- ensure-fresh [$q cache val]
  (let [entry (@cache val)
        expired (if (contains? :expired entry)
                    ((:expired entry))
                    true)]
    (if expired
        (-> $q
            (.when ((:refresh entry)))
            (.then (fn [new-val]
                     (swap! cache assoc val {:policy (:policy entry)
                                             :refresh (:refresh entry)
                                             :value new-val
                                             :expired ((:policy entry))})
                     new-val)))
        (.when $q (:value entry)))))

(defn- get-val [$q cache val]
  (if (contains? @cache val)
    (ensure-fresh $q cache val)
    (.when $q nil)))

(defn- timed [$q cache name time refresh]
  (swap! cache assoc name {:policy (timed-expiry-policy time)
                           :refresh refresh})
  (get-val $q cache name))

(ng/service "asyncCache" [$q]
  (let [cache (atom {})]
    (this-as this
      (doto this
        (aset "get" (partial get-val $q cache))
        (aset "timed" (partial timed $q cache))))))