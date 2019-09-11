(future (Thread/sleep 4000)
        (println "prints after 4sec"))
(println "prints immediately")

;; future returns a reference value
;; deref the future to get result using `deref` or `@`(reader) macro
(let [result (future (println "this prints once")
                     (+ 1 1))]
  (println "deref: " (deref result))
  (println "@: " @result))

;; If future is not done yet at the time of deref, the thread calling deref blocks
(let [result (future (Thread/sleep 3000)
                     (+ 1 1))]
  (println "The result is: " @result)
  (println "It will be atleast 3sec before this is printed"))

;; time limit on future deref
(deref (future (Thread/sleep 1000) 0) 10 5) ;; wait for 10ms - if not resolved return 5

(realized? (future (Thread/sleep 1000)))
(let [f (future)]
  @f
  (realized? f))

;; Delays
(def jackson-delay
  (delay (let [message "Just call my name"] ;; defer execution
           (println "First deref: " message)
           message)))
(force jackson-delay) ;; force deferred execution - executed only once - result cached

;; use delay to fire statement the first time one future out of a group finishes
(def headshots ["serious.jpg" "fun.jpg" "playful.jpg"])
(defn email-user
  [email-address]
  (println "Sending headshot notification to" email-address))
(defn upload-document
  "Needs to be implemented"
  [headshot] true)
(let [notify (delay (email-user "foo@example.com"))]
  (doseq [headshot headshots]
    (futuer (upload-document headshot)
            (force notify))))
;; even though force runs 3 times, delay body will execute only once
;; delay result is cached

;; Promises
(def my-promise (promise))
(deliver my-promise (+ 1 2)) ;; can only deliver result to promise once
@my-promise

(def yak-butter-international
  {:store "Yak Butter Int"
   :price 90
   :smoothness 90})
(def butter-than-nothing
  {:store "Butter than Nothing"
   :price 150
   :smoothness 83})
(def baby-got-yak
  {:store "Baby Got Yak"
   :price 94
   :smoothness 99})

(defn mock-api-call
  [result]
  (Thread/sleep 1000)
  result)

(defn satisfactory?
  "If butter meets our criteria, return it else return false"
  [butter]
  (and (<= (:price butter) 100)
       (>= (:smoothness butter) 97)
       butter))
(time (some (comp satisfactory? mock-api-call)
            [yak-butter-international butter-than-nothing baby-got-yak]))

;; Use promise & futures to perform each check in separate thread
(time
 (let [butter-promise (promise)]
   (doseq [butter [yak-butter-international butter-than-nothing baby-got-yak]]
     (future (if-let [satisfactory-butter (satisfactory? butter)]
               (deliver butter-promise butter))))
   (println "And the winner is: " @butter-promise)))
;; timeout promise
(let [p (promise)]
  (deref p 100 "timed out")) ;; wait for 100ms else return timed out

;; Javascript-style callbacks
(let [wisdom-promise (promise)]
  (future (println "Here's some wisdom:" @wisdom-promise)) ;; this blocks until deliver
  (Thread/sleep 100)
  (deliver wisdom-promise "This is the way to success."))

;; Future, delays & promises are ways to manage concurrency in Clojure
