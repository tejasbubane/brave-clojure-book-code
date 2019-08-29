(ns-name *ns*)

;; show list of bindings (interned vars) in current namespace
(ns-interns *ns*) ;; empty

(def books ["1984", "The Handmaid's tale"])
(ns-interns *ns*) ;; adds books to current namespace
(get (ns-interns *ns*) 'books) ;; ns-interns is just a map - use get to get interned vars
(ns-map *ns*) ;; full map namespace uses for lookup up any var

(deref #'user/books) ;; same as calling books directly

(def books ["Sacred Games", "Fairy tales"]) ;; name collision - reference updated

;; 3 ways to create namespace
(create-ns 'taxonomy)
(ns-name (create-ns 'taxonomy))

;; (in-ns 'foo) ;; create namespace (if not exists) & switch to it
(def cheese ["Mozzerella" "Cheddar" "Gouda"])
;; use cheese in core namespace using fully qualified name: foo/cheese

;; (clojure.core/refer 'foo)
;; updates current namespace map to allow reference to other namespace without fully qualified name

;; defn- (with hiphen) creates private function
(defn- private-function
  "example private function - does nothing"
  [])

(clojure.core/alias 's 'myapp.max.s) ;; short name for namespaces
