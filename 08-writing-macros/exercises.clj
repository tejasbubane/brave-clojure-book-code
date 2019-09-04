;; 1. Write a macro `when-valid` so that it behaves similar to `when`.

(defn render
  [string]
  (println string))

(def order-details
  {:name "Tejas Bubane"
   :email "tejas.example.com"})

(def order-details-validations
  {:name
   ["Please enter a name" not-empty]

   :email
   ["Please enter an email" not-empty

    "Your email doesn't look like an email address"
    #(or (empty? %) (re-seq #"@" %))]})

(defn error-messages-for
  "Return a seq of error messages for single value"
  [to-validate message-validator-pairs]
  (map first (filter #(not ((second %) to-validate))
                     (partition 2 message-validator-pairs))))

(defn validate
  "Returns a map with a vector of errors for each key"
  [to-validate validations]
  (reduce (fn [errors validation]
            (let [[fieldname validation-check-groups] validation
                  value (get to-validate fieldname)
                  error-messages (error-messages-for value validation-check-groups)]
              (if (empty? error-messages)
                errors
                (assoc errors fieldname error-messages))))
          {}
          validations))

(defmacro when-valid
  "Handle validation more concisely"
  [to-validate validations errors-name & body]
  `(let [~errors-name (validate ~to-validate ~validations)]
     (when (not (empty? ~errors-name))
       ~@body)))

(not (empty? (validate order-details order-details-validations)))

(when-valid order-details order-details-validations foo
                         (println "It's a success!")
                         (render "success"))

;; 2. Implement or as a macro
(defmacro or
  ([] true)
  ([first] first)
  ([first & rest]
   `(if ~first true (or ~@rest))))
(or (= 1 1) (= 2 2))
(or true (> 3 4) false)
(or (> 3 4) false)

;; 3. write macro that defines arbitrary number of attribute-retrieving functions
;; Similar to ones done in chapter 5
;; (def c-int (comp :intelligence :attributes))
;; (def c-evil (comp :evil :attributes))
(def character
  {:name "Smith"
   :attributes {:intelligence 100
                :strength 90
                :dexterity 40}})

(defmacro defattrs
  [& args]
  (let [varnames (partition 2 args)]
    (cons 'do (map (fn [[name key]]
                     `(def ~name (comp ~key :attributes)))
                   varnames))))

(macroexpand '(defattrs c-int :intelligence
               c-str :strength
               c-dex :dexterity))
(defattrs c-int :intelligence
               c-str :strength
               c-dex :dexterity)
(c-int character)
(c-str character)
(c-dex character)
