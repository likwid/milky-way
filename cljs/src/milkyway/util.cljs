(ns milkyway.util
    (:refer-clojure :exclude [js->clj]))

(declare keywordize)
(defmulti keywordize-val map?)
(defmethod keywordize-val true [obj] (keywordize obj))
(defmethod keywordize-val false [obj] obj)

(defn- keywordize [m]
    (zipmap (map keyword (keys m)) (map keywordize-val (vals m))))

(def js->clj (comp keywordize cljs.core/js->clj))

(defn vm-cycle [$scope f]
    (let [vm (js->clj (aget $scope "vm"))]
        (aset $scope "vm" (clj->js (conj vm (f vm))))))