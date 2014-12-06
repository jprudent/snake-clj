(ns snake-clj.events
  (:import (java.util Random))
  (:require [snake-clj.core :refer :all]
            [snake-clj.matrix :as matrix]
            [clojure.data.generators :as gen]))

(defn- event [type id]
  {:event-type type
   :id         id})

(defn game-started [id seed]
  (assoc
    (event :game-started id)
    :seed seed))

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
  (gen/weighted {:apple      1
                 :wall       5
                 (fn [] nil) 20}))

(defn- rand-world []
  (partition 5 (for [_ (range 25)]
                 (rand-cell))))

(defn- rand-heading []
  (gen/one-of :up :down :right :left))

(defn- rand-position [matrix]
  (let [f (fn [] (gen/uniform 0 5))]
    (gen/tuple f f)))

(defmethod handle-event :game-started
  [{seed :seed}]
  (binding [gen/*rnd* (Random. seed)]
    (let [world (rand-world)]
      (snake-state world (rand-position world) (rand-heading)))))