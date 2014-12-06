(ns snake-clj.events
  (:require [snake-clj.core :refer :all]
            [snake-clj.matrix :as matrix]))

(defn- event [type id]
  {:event-type type
   :id         id})

(defn game-started [id]
  (event :game-started id))

(defn hit-wall [id]
  (event :hit-wall id))

(defn ate-tail [id]
  (event :ate-tail id))

(defn ate-apple [id]
  (event :ate-apple id))

(defn turned-right [id]
  (event :turned-right id))

(defn turned-left [id]
  (event :turned-left id))

(defn gone-ahead [id]
  (event :gone-ahead id))

(defmulti handle-event (fn [event] (:event-type event)))

;; GAME STARTED

(defn- rand-cell []
  (let [r (rand-int 100)]
    (cond
      (<= r 10) :apple
      (and (>= r 11) (<= r 35)) :wall
      :else nil)))

(defn- rand-world []
  (partition 5 (for [_ (range 25)]
                 (rand-cell))))

(defn- rand-heading []
  (condp = (rand-int 4)
    0 :top
    1 :bottom
    2 :right
    4 :left))

(defmethod handle-event :game-started
  [_]
  (let [world (rand-world)]
    (snake-state world (matrix/rand-position world) (rand-heading))))