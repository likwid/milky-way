(ns clojangular.angular)

(def ^:private add-ng-export (partial map #(symbol (str % "/ng-export"))))

(defn- invoke-with [module components]
  (map #(list % module) components))

(defmacro module [module dependencies & components]
  (let [referenced-components (-> components add-ng-export ((partial invoke-with module)))
        module-declaration `(.module js/angular ~module (cljs.core/clj->js ~dependencies))]
    `(do ~module-declaration ~@referenced-components)))

(def ^:private to-angular-deps (comp vec (partial map str)))

(defn component [type c-form a-forms b-forms]
  (let [angular-deps (to-angular-deps a-forms)
        component-main `(defn ~'ng-component [~@a-forms] ~@b-forms)
        mini-safe-decl (conj angular-deps 'ng-component)]
    `(do
       ~component-main
       (defn ~'ng-export [module#]
         (-> js/angular (.module module#)
                        (~(symbol (str "." type)) ~c-form
                                (cljs.core/clj->js ~mini-safe-decl)))))))

(defn component-decl [type]
  `(defmacro ~type [~'name ~'args & ~'forms]
     (component '~type ~'name ~'args ~'forms)))

(defmacro defcomponents [& types]
  `(do ~@(map component-decl types)))

(defcomponents controller
               directive
               service)

(defmacro config [args & forms]
  (let [angular-deps (to-angular-deps args)
        config-main `(defn ~'ng-config [~@args] ~@forms)
        mini-safe-decl (conj angular-deps 'ng-config)]
    `(do
       ~config-main
       (defn ~'ng-export [module#]
         (-> js/angular (.module module#)
                        (.config (cljs.core/clj->js ~mini-safe-decl)))))))