(ns clojangular.angular)

(def ^:private add-ng-export (partial map #(symbol (str % "/ng-export"))))

(defn- invoke-with [module components]
  (map #(read-string (format "(%s \"%s\")" % module)) components))

(defmacro module [module dependencies & components]
  (let [referenced-components (-> components add-ng-export
                                             ((partial invoke-with module)))
        module-declaration `(.module js/angular ~module
                                                (cljs.core/clj->js ~dependencies))
        forms (cons module-declaration referenced-components)]
    `(do ~@forms)))

(def ^:private to-angular-deps (comp vec (partial map str)))

(defmacro controller [controller args & forms]
  (let [angular-deps (to-angular-deps args)
        controller-main `(defn ~'ng-controller [~@args] ~@forms)
        mini-safe-decl (conj angular-deps 'ng-controller)]
    `(do
       ~controller-main
       (defn ~'ng-export [module#]
         (-> js/angular (.module module#)
                        (.controller ~controller
                                     (cljs.core/clj->js ~mini-safe-decl)))))))

(defmacro config [args & forms]
  (let [angular-deps (to-angular-deps args)
        config-main `(defn ~'ng-config [~@args] ~@forms)
        mini-safe-decl (conj angular-deps 'ng-config)]
    `(do
       ~config-main
       (defn ~'ng-export [module#]
         (-> js/angular (.module module#)
                        (.config (cljs.core/clj->js ~mini-safe-decl)))))))