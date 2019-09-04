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

(validate order-details order-details-validations)

(let [errors (validate order-details order-details-validations)]
  (if (empty? errors)
    (println :success)
    (println :failure errors)))
;; This pattern we need to abstract

(defmacro if-valid
     "Handle validation more concisely"
     [to-validate validations errors-name & then-else]
     `(let [~errors-name (validate ~to-validate ~validations)]
        (if (empty? ~errors-name)
          ~@then-else)))

(macroexpand '(if-valid order-details order-details-validations errors
                       (println :success)
                       (println :failure errors)))
