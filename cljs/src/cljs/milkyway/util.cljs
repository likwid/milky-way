(ns milkyway.util)

(declare keywordize)

(defn- keywordize-val [v]
  (if (map? v) (keywordize v) v))

(defn- keywordize [m]
  (zipmap (map keyword (keys m)) (map keywordize-val (vals m))))

(def obj->map (comp keywordize js->clj))

(defn vm-cycle [$scope f]
  (let [vm (obj->map (aget $scope "vm"))]
    (aset $scope "vm" (clj->js (conj vm (f vm))))))