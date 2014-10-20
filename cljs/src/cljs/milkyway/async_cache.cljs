(ns milkyway.async-cache)

(def ^:private cache (atom {}))

(defn- timed-expiry [time]
  (let [then (.getTime (js/Date.))]
    #(> (- (.getTime (js/Date.)) then) time)))

(defn- timed-expiry-policy [time]
  #(timed-expiry time))

(defn- ensure-fresh [val]
  (let [entry (@cache val)
        expired (if (contains? :expired entry)
                    ((:expired entry))
                    true)]
    (if expired
        (-> js/Q
            (.when ((:refresh entry)))
            (.then (fn [new-val]
                     (swap! cache assoc val {:policy (:policy entry)
                                             :refresh (:refresh entry)
                                             :value new-val
                                             :expired ((:policy entry))})
                     new-val)))
        (.when js/Q (:value entry)))))

(defn get-val [val]
  (if (contains? @cache val)
    (ensure-fresh val)
    (.when js/Q nil)))

(defn timed [name time refresh]
  (swap! cache assoc name {:policy (timed-expiry-policy time)
                           :refresh refresh})
  (get-val name))