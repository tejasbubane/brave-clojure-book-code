(def fred (atom {:hunger-level 0
                 :rating 0}))
@fred ;; unlike futures, delays, promises - unreferencing atoms never blocks

(let [person @fred]
  (if (>= (:rating person) 50)
    (future (println (:hunger-level person)))))

;; swap! to change reference of atom
;; all values are immutable - atoms are references to these atomic values which can change
(swap! fred
       (fn [current-state]
         (merge-with + current-state {:hunger-level 1})))
@fred

(defn increase-rating
  [state increase-by]
  (merge-with + state {:rating increase-by}))
(increase-rating @fred 10)
@fred ;; fred is unchanged because we did not use swap!

(swap! fred increase-rating 20)
@fred ;; now fred is updated

;; or do it all in one step
(swap! fred update-in [:rating] + 10)

(let [num (atom 1)
      s1 @num]
  (swap! num inc)
  (println "State 1:" s1) ;; previous state is still available (in another variable)
  (println "Current state:" @num))
;; swap updates are `compare-and-set` and synchronous
;; retry if value changed between lookup & update - is blocking operation

;; Watches on atoms
(defn speed
  [person]
  (* (:hunger-level person)
     (- 100 (:rating person))))

(defn speed-alert
  [key watched old-state new-state]
  (let [mph (speed new-state)]
    (if (> mph 500)
      (do
        (println "Slow down kiddo!")
        (println "You are running at " mph)
        (println "This message is with the help of " key))
      (do
        (println "Run faster")
        (println "Your speed is " mph)
        (println "Go! you can do this!")))))
(add-watch fred :fred-speed-check speed-alert)
(swap! fred update-in [:hunger-level] + 5)

;; Validators on atoms
(defn percent-rating-validator
  [{:keys [rating]}]
  (and (>= rating 0)
       (<= rating 100)))
(def bob
  (atom {:hunger-level 20 :rating 100} :validator percent-rating-validator))
(swap! bob update-in [:rating] + 20) ;; throws invalid state error

;; refs - STM implementation
(def apple-varieties
  #{"shimla" "kashmir" "washington" "nepal" "california"})

(defn apple-count
  [apple-variety count]
  {:variety apple-variety
   :count count})

(defn generate-apple-inventory
  [name]
  {:name name
   :apples #{}})

(def first-inventory (ref (generate-apple-inventory "First")))
(def seller1 (ref {:name "John"
                  :apples (set (map #(apple-count % 2) apple-varieties))}))
(def seller2 (ref {:name "Mike"
                  :apples (set (map #(apple-count % 2) apple-varieties))}))
(:apples @seller)

(defn transfer-apples
  [seller1 seller2]
  (dosync ;; dosync initiates the transaction
   (when-let [pair (some #(if (= (:count %) 2) %) (:apples @seller2))]
     (let [updated-count (apple-count (:variety pair) 1)]
       (alter seller1 update-in [:apples] conj updated-count)
       (alter seller2 update-in [:apples] disj pair)
       (alter seller2 update-in [:apples] conj updated-count)))))
;; altering ref change isn't immediately visible in a transaction

(transfer-apples seller1 seller2)
(:apples @seller1)
(:apples @seller2)

;; in-transaction state
(def counter (ref 0))
(future
  (dosync
   (alter counter inc)
   (println @counter)
   (Thread/sleep 500)
   (alter counter inc)
   (println @counter)))
(Thread/sleep 250)
(println @counter) ;; main thread is outside transaction & doesn't see increment inside until transaction is done
;; entire transaction is retried if commit sees underneath value has changed

;; Vars
(def ^:dynamic *notification-address* "dobby@elf.org")
;; ^:dynamic makes it dynamic
;; name should be enclosed in *s
(binding [*notification-address* "test@elf.org"]
  *notification-address*) ;; local binding change
*notification-address* ;; dobby - unchanged

;; Use of dynamic var
(defn notify
  [message]
  (str "TO: " *notification-address* "\n"
       "MESSAGE: " message))
(notify "I fell.")

(binding [*notification-address* "test@elf.org"]
  (notify "test!")) ;; local stubs

;; *out* represents STDOUT
;; re-bind it to file instead
(binding [*out* (clojure.java.io/writer "print-output")]
  (println "A man who carries a cat by the tail learns")
  (slurp "print-output"))
;; use `set!` for global binding

;; Var root
(def power-source "hair")
(alter-var-root #'power-source (fn [_] "seven")) ;; never do this
power-source ;; changed value

;; pmap
(defn always-1 [] 1)
(take 5 (repeatedly always-1))
(take 5 (repeatedly (partial rand-int 10)))
(def alphabet-length 26)
(def letters (mapv (comp str char (partial + 65)) (range alphabet-length)))
(defn random-string
  "Generates random string of specified length"
  [length]
  (apply str (take length (repeatedly #(rand-nth letters)))))
(defn random-string-list
  [list-length string-length]
  (doall (take list-length (repeatedly (partial random-string string-length)))))
(def orc-names (random-string-list 3000 7000))
(time (dorun (map clojure.string/lower-case orc-names)))
(time (dorun (pmap clojure.string/lower-case orc-names)))

(def orc-name-abbrevs (random-string-list 20000 300))
(time (dorun (map clojure.string/lower-case orc-name-abbrevs)))
(time (dorun (pmap clojure.string/lower-case orc-name-abbrevs))) ;; pmap takes more time
;; because of thread overhead
;; to overcome this increase grain size - each thread performs more that one operations
(def numbers [1 2 3 4 5 6 7 8 9 10])
(partition-all 3 numbers)
(pmap inc numbers) ;; grain-size = 1
(apply concat
       (pmap (fn [number-group] (doall (map inc number-group)))
             ;; use doall to force sequence inside thread
             (partition-all 3 numbers)))
(time
 (dorun
  (apply concat
         (pmap (fn [name] (doall (map clojure.string/lower-case name)))
               (partition-all 1000 orc-name-abbrevs)))))
